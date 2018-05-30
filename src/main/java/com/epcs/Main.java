package com.epcs;

import com.kulu.activemq.MQMessage;
import com.kulu.utils.JSONUtils;

import cn.tpson.device.watch.cmd.InHeartRateCmd;

public class Main {
	public void start() {
		System.out.println("*************WatchMain start****************");
		new WatchMain().start();
		System.out.println("*************CarMain start****************");
		new CarMain().start();
		System.out.println("*************WatchMain,CarMain start over****************");
	}

	public void test() {
		String test = "@G#@,V01,43,108101712007180,9460004820707903,20180315170109,4,460;0;29601;8431;-33|460;0;29601;7652;-62|460;0;29601;12522;-36|460;0;29820;9903;-22,6,8c:21:0a:7e:22:52;ffffffffffffffff;-64|50:fa:84:56:ad:09;00430043002d0048006f006d0065002d0032002e00340047;-72|d8:b0:4c:b6:ea:e0;00470061006f00450072002d005800430053004700300031;-73|d4:68:ba:07:55:21;0042004100490043004800550041004e0047;-73|34:96:72:c0:c1:b7;006700640074006b;-89|08:9b:4b:93:e7:8d;0059004c00490054002d0032002e00340047;-89,@R#@";
		String[] strs = test.split(",");
		for (String str : strs) {
			System.out.println(str);
		}

		InHeartRateCmd data = new InHeartRateCmd("123123", "123123");
		MQMessage message = new MQMessage();
		message.setEqpid(data.getToken());
		message.setTag("data");
		message.setDatabody(data);
		System.out.println(JSONUtils.toJSONString(message));

	}

	public static void main(String[] args) {
		new Main().start();
		// new Main().test();
	}
}
