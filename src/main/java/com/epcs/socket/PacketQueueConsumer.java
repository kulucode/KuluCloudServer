package com.epcs.socket;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.epcs.utils.parser2.CommPacket;
import com.epcs.utils.parser2.PacketGeneralResponse;
import com.epcs.utils.parser2.PacketHeartBeart;
import com.epcs.utils.parser2.PacketLocationBatchReport;
import com.epcs.utils.parser2.PacketLocationReport;
import com.epcs.utils.parser2.PacketQueryTermAttr;
import com.epcs.utils.parser2.PacketQueryTermParas;
import com.epcs.utils.parser2.PacketTermAuth;
import com.epcs.utils.parser2.PacketTermRegister;
import com.epcs.utils.parser2.UtilsConvert;
import com.kulu.activemq.MQCarMessage;
import com.kulu.utils.JSONUtils;

import cn.tpson.device.watch.codec.StrUtil;

public class PacketQueueConsumer implements Runnable {

	static Logger logger = Logger.getLogger(PacketQueueConsumer.class.getName());
	private PacketQueue queue;
	private WebSocketCarInfoQueue carInfoQueue;
	private volatile boolean shutdownRequested = false;

	public PacketQueueConsumer(PacketQueue queue, WebSocketCarInfoQueue carInfoQueue) {
		this.queue = queue;
		this.carInfoQueue = carInfoQueue;
	}

	public final void shutdownRequest() {
		shutdownRequested = true;
	}

	public void run() {
		while (!shutdownRequested) {
			// 消费SB
			logger.info(">>>>>>>[PacketQueueConsumer] waiting for packakge! Queue size:" + queue.queue.size());
			SessionPacketBean packet = queue.consume();
			SessionBean sessionBean = SocketHandler.getSessionBean(packet.getSession());

			if (sessionBean != null) {
				synchronized (sessionBean) {
					packetHandler(packet);
				}
			}
		}

	}

	private void packetHandler(SessionPacketBean sp) {
		byte[] responseMsg = null;
		byte[] msgBody = null;
		CommPacket curPacket = sp.getCurCommPacket();
		if (curPacket != null && curPacket.isValidPacket()) {
			SessionBean sessionBean = SocketHandler.getSessionBean(sp.getSession());
			// logger.info(">>>>>> start to handle received handle package msgid
			// is:" +
			// curPacket.toString()
			// + ",sessionBean:" + sessionBean);
			logger.info("msg_id:" + PacketTermRegister.MSG_ID);
			switch (curPacket.msgId) {
			case PacketTermRegister.MSG_ID:// 注册...
				// 制造商ID+终端型号+设备ID 为主键
				System.out
						.println("********************注册" + curPacket.termPhoneNumber + "***************************");
				PacketTermRegister packetTermRegister = new PacketTermRegister(curPacket.msgBody);
				String termId = StrUtil.bytesToString(packetTermRegister.termId);
				SocketHandler.saveSession(termId, sessionBean.getSession());
				System.out.println("termId:" + termId);
				System.out.println(StrUtil.bytesToHex(curPacket.msgBody));
				System.out.println(JSONUtils.toJSONString(packetTermRegister));
				System.out.println("***********************************************");
				MQCarMessage.reg(curPacket, termId, packetTermRegister);
				return;
			case PacketTermAuth.MSG_ID:// 重新连接
				System.out.println(
						"********************重新连接" + curPacket.termPhoneNumber + "***************************");
				PacketTermAuth packetTermAuth = new PacketTermAuth(curPacket.msgBody);
				System.out.println(JSONUtils.toJSONString(packetTermAuth));
				System.out.println("***********************************************");
				SocketHandler.saveSession(packetTermAuth.authKey, sessionBean.getSession());
				MQCarMessage.authKeyLogin(curPacket, packetTermAuth.authKey);
				return;
			case PacketLocationReport.MSG_ID:// 定位
				System.out
						.println("********************定位" + curPacket.termPhoneNumber + "***************************");
				PacketLocationReport packetLocationReport = new PacketLocationReport(curPacket.msgBody);
				System.out.println(JSONUtils.toJSONString(packetLocationReport));
				System.out.println("***********************************************");
				handleLocationPacket(curPacket, sessionBean, packetLocationReport);
				break;
			case PacketLocationBatchReport.MSG_ID:// 多个位置
				PacketLocationBatchReport batchLoc = new PacketLocationBatchReport(curPacket.msgBody);
				if (batchLoc.isValidPacket()) {
					if (batchLoc.locationType == PacketLocationBatchReport.TYPE_NORMAL_BATCH) {
						if (batchLoc.locations != null) {
							// 正常批量上传时处理所有点
							for (PacketLocationReport locationReport : batchLoc.locations) {
								handleLocationPacket(curPacket, sessionBean, locationReport);
							}
						}
					} else if (batchLoc.locationType == PacketLocationBatchReport.TYPE_BLIND_ZONE_MISSED) {
						if (batchLoc.locations != null) {
							// 盲区补传时只处理头尾两个点
							int batchSize = batchLoc.locations.size();
							for (int i = 0; i < batchSize; i++) {
								if (i == 0 || i == batchSize - 1) {
									PacketLocationReport locationReport = batchLoc.locations.get(i);
									handleLocationPacket(curPacket, sessionBean, locationReport);
								}
							}
						}
					}
				} else {
					logger.info(" invalid PacketLocationBatchReport packet");
				}
				break;
			case PacketHeartBeart.MSG_ID:// 心跳
				System.out
						.println("********************心跳" + curPacket.termPhoneNumber + "***************************");
				msgBody = PacketHeartBeart.formPlatformMsgBody();
				responseMsg = CommPacket.formResponsePacket(PacketGeneralResponse.RESPONSE_MSG_ID,
						curPacket.termPhoneNumber, curPacket.msgSeq, msgBody);
				synchronized (SocketHandler.LOCK) {
					SocketHandler.heartbeatList.put(sp.getSession(), new Date());// 更新时间
				}
				break;
			case PacketQueryTermParas.RESPONSE_MSG_ID:// 查询参数
				PacketQueryTermParas packetQueryTermParas = new PacketQueryTermParas(curPacket.msgBody);
				break;
			case PacketQueryTermAttr.RESPONSE_MSG_ID:// 查询属性
				PacketQueryTermAttr termAttr = new PacketQueryTermAttr(curPacket.msgBody);
				logger.info(termAttr);
				break;
			}
		} else {
			logger.info(" invalid packet replay common msg");
		}
		// 每条信息都需要回复
		if (responseMsg == null) {
			if (curPacket != null) {
				msgBody = PacketGeneralResponse.formPlatformMsgBody(curPacket.msgSeq, curPacket.msgId,
						PacketGeneralResponse.SUCESS);
				responseMsg = CommPacket.formResponsePacket(PacketGeneralResponse.RESPONSE_MSG_ID,
						curPacket.termPhoneNumber, curPacket.msgSeq, msgBody);
			} else {
				// replay for null packet
				msgBody = PacketGeneralResponse.formPlatformMsgBody(1, 1, PacketGeneralResponse.SUCESS);
				responseMsg = CommPacket.formResponsePacket(PacketGeneralResponse.RESPONSE_MSG_ID, "00000000", 1,
						msgBody);
			}
			// logger.info("send common response msg");
		}
		// response
		sendMessage(sp.getSession(), responseMsg);
		// logger.info("<<<<<<<< ******* End of process packet *******");
	}

	public static void sendMessage(String token, byte[] responseMsg) {
		if (SocketHandler.tokenMap.containsKey(token)) {
			sendMessage(SocketHandler.tokenMap.get(token), responseMsg);
		} else {
			System.out.println("车载不存在token" + token);
		}
	}

	public static void sendMessage(IoSession session, byte[] responseMsg) {
		if (session != null && responseMsg != null) {
			try {
				System.out.println("车载输出:" + StrUtil.bytesToHex(responseMsg));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			IoBuffer reponseBuffer = IoBuffer.allocate(responseMsg.length).setAutoExpand(true);
			reponseBuffer.put(responseMsg);
			reponseBuffer.flip();
			WriteFuture future = session.write(reponseBuffer);
			// Wait until the message is completely written out to the O/S
			// buffer.
			try {
				future.await(5 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("<<<< write exception:" + e.toString());
			}

			if (future.isWritten()) {
				// The message has been written successfully.
				// logger.info("<<<<<<<<write success:");// +
				// UtilsConvert.byteToHex0x(responseMsg));
			} else {
				// The messsage couldn't be written out completely for some
				// reason.
				// (e.g. Connection is closed)
				logger.info("<<<<<<<<write failed:" + UtilsConvert.byteToHex(responseMsg));
			}
			reponseBuffer.free();
		} else {
			logger.info("session is null, do nothing");
		}
	}

	// 处理位置信息
	public void handleLocationPacket(CommPacket curPacket, SessionBean sessionBean,
			PacketLocationReport packetLocationReport) {
		if (curPacket == null || sessionBean == null || packetLocationReport == null) {
			logger.info("null found when handleLocationPacket");
			return;
		}
		// 发送定位信息去服务器
		MQCarMessage.sendLocationPacket(sessionBean.getSession(), packetLocationReport);
	}

	public WebSocketCarInfoQueue getCarInfoQueue() {
		return carInfoQueue;
	}

	public void setCarInfoQueue(WebSocketCarInfoQueue carInfoQueue) {
		this.carInfoQueue = carInfoQueue;
	}
}
