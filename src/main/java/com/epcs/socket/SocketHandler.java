package com.epcs.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.epcs.utils.parser2.CommPacket;
import com.epcs.utils.parser2.PacketParser;
import com.kulu.activemq.MQCarMessage;

import cn.tpson.device.watch.cmd.BaseCommand;
import cn.tpson.device.watch.codec.StrUtil;
import cn.tpson.device.watch.core.WatchHandler;

public class SocketHandler extends IoHandlerAdapter {

	static Logger logger = Logger.getLogger(SocketHandler.class.getName());
	public static final long sleepTime = WatchHandler.sleepTime;// 30秒检测一次
	public static final long outTime = WatchHandler.outTime;// 3分钟
	public static final String LOCK = "LOCK";
	public static HashMap<IoSession, Date> heartbeatList = new HashMap<IoSession, Date>();// 心跳用户列表
	public boolean isRun = true;

	public static final String LOGIN_KEY = "loginKey";
	public static final Map<Long, SessionBean> clientMap = new HashMap<>();
	public static HashMap<String, Long> userIds = new HashMap<String, Long>();// 在线列表id:user
	public static final Map<String, IoSession> tokenMap = new HashMap<>();

	public static SessionBean getSessionBean(IoSession session) {
		return clientMap.get(session.getId());
	}

	private PacketQueue packetQueue;

	public SocketHandler(PacketQueue packetQueue) {
		this.packetQueue = packetQueue;
		init();
	}

	public void stop() {
		isRun = false;
	}

	public void init() {
		// 处理心跳
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRun) {
					try {
						Thread.sleep(sleepTime);// 1分钟
						synchronized (LOCK) {
							Date now = new Date();
							for (IoSession session : heartbeatList.keySet()) {
								if (now.getTime() - heartbeatList.get(session).getTime() > outTime) {
									session.close(true);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		SessionBean sb = new SessionBean();
		sb.setSession(session);
		// get fenc
		clientMap.put(session.getId(), sb);
		logger.info("[Open] new session:" + session + ", current session num:" + clientMap.size());
		synchronized (SocketHandler.LOCK) {
			heartbeatList.put(session, new Date());// 刚刚创建
		}

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		logger.info("[exceptionCaught ] found exception in session id: " + session.getId() + ": " + cause.toString());
		try {
			super.exceptionCaught(session, cause);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		logger.info("[messageReceived ++++] received package from device in session:" + session.getId());
		SessionBean sb = clientMap.get(session.getId());
		if (sb == null)
			return;

		IoBuffer ioBuffer = (IoBuffer) message;
		int r = ioBuffer.remaining();
		byte[] msgBytes = new byte[r];
		ioBuffer.get(msgBytes, 0, r);
		System.out.println("收到消息" + StrUtil.bytesToHex(msgBytes));
		// CommPacket packet = PacketParser.parsePacket(msgBytes);
		// // enqueue packet
		// SessionPacketBean sp = new SessionPacketBean();
		// sp.setCurCommPacket(packet);
		// sp.setSession(session);
		// packetQueue.produce(sp);
		// // logger.info(
		// // "[messageReceived ----] ioBuffer limit():" + ioBuffer.limit() +
		// // ",position():" +
		// // ioBuffer.position()
		// // + ", remaining():" + ioBuffer.remaining() + ", array():" +
		// // ioBuffer.array().length);
		// logger.info("[messageReceived ----] packet:" + packet + ",queue
		// size:" + packetQueue.queue.size());
		try {
			sb.dataCache.write(msgBytes);
			byte[] data = sb.dataCache.toByteArray();
			int startIndex = handleData(data, 0, session);// 处理数据
			if (startIndex > 0) {
				IoBuffer in = IoBuffer.wrap(data);
				byte[] bytes = new byte[data.length - startIndex];// 剩下的数据
				in.position(startIndex);
				in.get(bytes);
				sb.dataCache.reset();
				sb.dataCache.write(bytes);
				System.out.println("剩下的数据:" + new String(bytes, CHARSET_NAME));
			} else if (startIndex < 0) {// 清空数据,没有找到开始下标
				sb.dataCache.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final static String CHARSET_NAME = "utf-8";
	public final static byte[] START_BYTE = new byte[] { 0x7E };
	public final static byte[] END_BYTE = START_BYTE;

	public int handleData(byte[] data, int startIndex, IoSession session) {
		boolean findStart = false;
		int index = 0;
		for (int i = startIndex; i < data.length; i++) {
			if (data[i] == START_BYTE[index]) {
				index++;
				if (START_BYTE.length == index) {// 成功匹配
					startIndex = i - START_BYTE.length + 1;
					findStart = true;
					break;
				}
			} else {
				index = 0;
			}
		}
		if (findStart) {
			int end_index = -1;
			index = 0;
			for (int i = startIndex + START_BYTE.length; i < data.length; i++) {
				if (data[i] == END_BYTE[index]) {
					index++;
					if (END_BYTE.length == index) {// 成功匹配
						end_index = i + 1;
						break;
					}
				} else {
					index = 0;
				}
			}
			IoBuffer in = IoBuffer.wrap(data);
			in.position(startIndex);
			if (end_index > 0) {// 成功找到数据
				int length = end_index - startIndex;
				byte[] bytes = new byte[length];
				in.get(bytes);
				System.out.println("成功找到车载数据" + StrUtil.bytesToHex(bytes));

				CommPacket packet = PacketParser.parsePacket(bytes);
				// enqueue packet
				SessionPacketBean sp = new SessionPacketBean();
				sp.setCurCommPacket(packet);
				sp.setSession(session);
				packetQueue.produce(sp);

				logger.info("[messageReceived ----]  packet:" + packet + ",queue size:" + packetQueue.queue.size());
				return handleData(data, end_index, session);// 处理下一个数据
			} else {
				return startIndex;
			}
		} else {
			return -1;
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("[messageSent] session id:" + session.getId() + ": send message ->" + message.toString());
	}

	public static void saveSession(String loginKey, IoSession session) {
		if (loginKey != null && !loginKey.equals("")) {
			session.setAttribute(LOGIN_KEY, loginKey);
			userIds.put(loginKey, session.getId());
			if (tokenMap.containsKey(loginKey)) {
				IoSession oldSession = tokenMap.get(loginKey);
				if (oldSession.getId() != session.getId()) {// 不是同一个会话
					System.out.println("车载存在相同session!");
					oldSession.close(true);
				}
			}
			tokenMap.put(loginKey, session);
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		long sessionId = session.getId();
		SessionBean sb = clientMap.get(session.getId());
		if (sb != null) {
			synchronized (sb) {
				if (sb.getLastCarInfo() != null)
					logger.info("[Closed] session of carinfo:" + sb.getLastCarInfo().toString()
							+ " closed. current session num:" + clientMap.size());
				clientMap.remove(sessionId);
			}
		}
		Object key = session.getAttribute(LOGIN_KEY);
		if (key != null) {
			String _key = (String) key;
			if (userIds.get(_key) != null && userIds.get(_key) == sessionId) {
				userIds.remove(_key);
				tokenMap.remove(_key);
				MQCarMessage.loginOut(_key);
			}
		}
		super.sessionClosed(session);
		logger.info("[Closed] session closed: " + session);
		synchronized (LOCK) {
			heartbeatList.remove(session);
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		logger.info("[Create] session created: " + session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		logger.info("[Idle] session enter idle: " + session);
	}
}
