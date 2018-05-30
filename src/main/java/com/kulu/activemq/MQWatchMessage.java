package com.kulu.activemq;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;

import cn.tpson.device.watch.cmd.BaseCommand;
import cn.tpson.device.watch.cmd.OutSetBloodPressureRangeCmd;
import cn.tpson.device.watch.cmd.OutSetHeartRateCmd;
import cn.tpson.device.watch.cmd.OutSetHeartRateRangeCmd;
import cn.tpson.device.watch.cmd.OutSetLocationCmd;
import cn.tpson.device.watch.cmd.OutSetStepCmd;
import cn.tpson.device.watch.core.WatchHandler;

public class MQWatchMessage {

	// 登录...
	public static void login(String token) {
		try {
			MQMessage message = new MQMessage();
			message.setEqpid(token);
			message.setTag("login");
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("token", token);
			message.setDatabody(params);
			WatchHandler.producer.sendWingservMessage(message);// 发送登录消息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loginCallback(boolean isLogin, String token, JSONObject data) {
		try {
			IoSession session = WatchHandler.users.get(token);
			if (session != null) {
				if (isLogin) {
					int hr_min = 112;// 心率
					int hr_max = 200;// 心率
					int hr_date = 10;// 心率
					int bpo_h = 165;// 血压
					int bpo_l = 36;// 血压
					// int bpo_date = 1;//血压
					int gps_date = 1;// 位置
					int step_date = 10;// 计步
					try {
						hr_min = data.getIntValue("hr_min");
						hr_max = data.getIntValue("hr_max");
						hr_date = data.getIntValue("hr_date");
						bpo_h = data.getIntValue("bpo_h");
						bpo_l = data.getIntValue("bpo_l");
						// bpo_date = data.getIntValue("bpo_date");
						gps_date = data.getIntValue("gps_date");
						step_date = data.getIntValue("step_date");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (hr_date > 0) {
						session.write(new OutSetHeartRateCmd(hr_date));// 心率上传间隔设置：45
					}
					if (gps_date > 0) {
						session.write(new OutSetLocationCmd(gps_date));// 位置数据上报周期设置：19
					}
					if (step_date > 0) {
						session.write(new OutSetStepCmd(step_date));// 计步上传间隔设置：46
					}
					session.write(new OutSetHeartRateRangeCmd(hr_max, hr_min));// （cmd：47）心率阀值设置
					// session.write(new OutSetBloodPressureRangeCmd(bpo_h,
					// bpo_l));// （cmd：112）血压校准参数设置
				} else {
					session.close(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			WatchHandler.producer.sendWingservMessage(message);// 发送登录消息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送数据
	public static void sendData(BaseCommand data) {
		try {
			MQMessage message = new MQMessage();
			message.setEqpid(data.getToken());
			message.setTag("data");
			message.setDatabody(data);
			WatchHandler.producer.sendWingservMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
