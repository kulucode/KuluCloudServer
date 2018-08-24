package com.kulu.activemq;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;

import com.epcs.socket.PacketQueueConsumer;
import com.epcs.socket.SocketHandler;
import com.epcs.utils.parser2.CommPacket;
import com.epcs.utils.parser2.PacketGeneralResponse;
import com.epcs.utils.parser2.PacketLocationReport;
import com.epcs.utils.parser2.PacketTermRegister;

import cn.tpson.device.watch.codec.StrUtil;
import cn.tpson.device.watch.core.WatchHandler;

public class MQCarMessage {

	public static HashMap<String, Object> getIndata(CommPacket commPacket) {
		HashMap<String, Object> indata = new HashMap<String, Object>();
		indata.put("msgId", commPacket.msgId);
		indata.put("msgSeq", commPacket.msgSeq);
		indata.put("phone", commPacket.termPhoneNumber);
		return indata;
	}

	// 车开始登录
	public static void reg(CommPacket commPacket, String termId, PacketTermRegister packetTermRegister) {
		try {
			String producerId = "00000";//StrUtil.bytesToString(packetTermRegister.producerId);
			String termType = "M51-DGNW-L";//StrUtil.bytesToString(packetTermRegister.termType);
			System.out.println("==========termId:" + termId + ",termType:" + termType + ",producerId:" + producerId);
			if (StrUtil.isEmpty(termId)/* || StrUtil.isEmpty(termType) || StrUtil.isEmpty(producerId)*/) {
				return;
			}
			int provinceId = packetTermRegister.provinceId;
			int cityId = packetTermRegister.cityId;
			int plateColor = packetTermRegister.plateColor;
			String carVIN = packetTermRegister.carVIN;
			String plateNumber = packetTermRegister.plateNumber;

			MQMessage message = new MQMessage();
			message.setEqpid(termId);// 终端ID
			message.setTag("reg");
			message.setIndata(getIndata(commPacket));

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", termId);
			params.put("provinceId", provinceId);// 数字
			params.put("cityId", cityId);// 数字
			params.put("producerId", producerId);
			params.put("termType", termType);
			params.put("plateColor", plateColor);// 数字
			params.put("carVIN", carVIN);// plateColor 为0时，有值
			params.put("plateNumber", plateNumber);// plateColor 不为0时，有值
			message.setDatabody(params);
			WatchHandler.producer.sendTruckservMessage(message);// 发送注册消息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void regCallback(boolean isLogin, String termId, String authKey, HashMap<String, Object> indata) {
		byte[] msgBody = null;
		int msgSeq = (int) indata.get("msgSeq");
		String phone = (String) indata.get("phone");
		if (isLogin) {
			System.out.println("车载注册成功!");
			msgBody = PacketTermRegister.formResponseMsgBody(msgSeq, PacketTermRegister.REG_SUCCESS, authKey);
		} else {
			msgBody = PacketTermRegister.formResponseMsgBody(msgSeq, PacketTermRegister.REG_CAR_NOTEXIST, "kulu" + termId);
		}
		if (SocketHandler.tokenMap.containsKey(termId)) {
			SocketHandler.userIds.remove(termId);
			IoSession session = SocketHandler.tokenMap.remove(termId);
			SocketHandler.saveSession(authKey, session);
			byte[] responseMsg = CommPacket.formResponsePacket(PacketTermRegister.RESPONSE_MSG_ID, phone, msgSeq,
					msgBody);
			PacketQueueConsumer.sendMessage(session, responseMsg);
		} else {
			System.out.println("车载session不存在");
		}
	}

	// 重新连接
	public static void authKeyLogin(CommPacket commPacket, String authKey) {
		try {
			MQMessage message = new MQMessage();
			message.setEqpid(authKey);
			message.setTag("authKeyLogin");
			message.setIndata(getIndata(commPacket));
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", authKey);
			message.setDatabody(params);
			WatchHandler.producer.sendTruckservMessage(message);// 发送登录消息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void authKeyLoginCallback(boolean isLogin, String token, HashMap<String, Object> indata) {
		byte[] msgBody = null;
		int msgSeq = (int) indata.get("msgSeq");
		int msgId = (int) indata.get("msgId");
		String phone = (String) indata.get("phone");
		if (isLogin) {
			// register success
			msgBody = PacketGeneralResponse.formPlatformMsgBody(msgSeq, msgId, PacketGeneralResponse.SUCESS);
		} else {
			// fail
			msgBody = PacketGeneralResponse.formPlatformMsgBody(msgSeq, msgId, PacketGeneralResponse.FAILED);
		}
		byte[] responseMsg = CommPacket.formResponsePacket(PacketGeneralResponse.RESPONSE_MSG_ID, phone, msgSeq,
				msgBody);
		PacketQueueConsumer.sendMessage(token, responseMsg);
	}

	// 退出...
	public static void loginOut(String token) {
		try {
			MQMessage message = new MQMessage();
			message.setEqpid(token);
			message.setTag("login_out");
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", token);
			message.setDatabody(params);
			WatchHandler.producer.sendTruckservMessage(message);// 发送登录消息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送位置
	public static void sendLocationPacket(IoSession session, PacketLocationReport location) {
		try {
			String token = (String) session.getAttribute(SocketHandler.LOGIN_KEY);
			if (token != null && !token.equals("")) {
				MQMessage message = new MQMessage();
				message.setEqpid(token);
				message.setTag("data");
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("type", 0);
				params.put("location", location);
				message.setDatabody(params);
				WatchHandler.producer.sendTruckservMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
