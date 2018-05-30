package com.epcs;

import java.util.HashMap;

import com.epcs.utils.parser2.CommPacket;
import com.epcs.utils.parser2.PacketLocationReport;
import com.epcs.utils.parser2.PacketTermRegister;
import com.kulu.activemq.MQMessage;
import com.kulu.utils.JSONUtils;

import cn.tpson.device.watch.codec.StrUtil;
import cn.tpson.device.watch.core.WatchHandler;

public class Test {

	public static HashMap<String, Object> getIndata() {
		HashMap<String, Object> indata = new HashMap<String, Object>();
		indata.put("msgId", "msgId");
		indata.put("msgSeq", "msgSeq");
		return indata;
	}

	public static void test2() {
		try {
			String termId = "2";
			MQMessage message = new MQMessage();
			message.setEqpid(termId);
			message.setTag("login");
			message.setIndata(getIndata());
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", termId);
			message.setDatabody(params);
			System.out.println("登录:" + JSONUtils.toJSONString(message));

			String token = "token";
			message.setEqpid(token);
			message.setTag("authKeyLogin");
			message.setIndata(getIndata());
			params = new HashMap<String, Object>();
			params.put("token", token);
			message.setDatabody(params);
			System.out.println("重连:" + JSONUtils.toJSONString(message));

			message = new MQMessage();
			message.setEqpid(token);
			message.setTag("data");
			params = new HashMap<String, Object>();
			params.put("type", 0);
			params.put("location", new PacketLocationReport());
			message.setDatabody(params);
			System.out.println("位置:" + JSONUtils.toJSONString(message));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test() {
		try {
			String str = "002B0064310000000064356D7633353000000000000000000000000000320000000000000034";
			byte[] data = StrUtil.hexStringToByte(str);
			System.out.println(str);
			System.out.println(data);

			PacketTermRegister packetTermRegister = new PacketTermRegister(data);
			System.out.println("producerId:" + StrUtil.bytesToString(packetTermRegister.producerId));
			System.out.println("termType:" + StrUtil.bytesToString(packetTermRegister.termType));
			System.out.println("termId:" + StrUtil.bytesToString(packetTermRegister.termId));
			System.out.println(StrUtil.bytesToHex(data));
			System.out.println(JSONUtils.toJSONString(packetTermRegister));

			str = "1460B203B20F62F2A84507F5262D47DBA38B";
			System.out.println(StrUtil.bytesToHex(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test();
		// test2();
	}

}
