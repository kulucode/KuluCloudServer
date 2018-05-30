package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * （cmd：14）心率周期上传（中间包含了实时电量）
 * 
 * @author tangbin
 *
 */
public class InHeartRateCmd extends BaseCommand {
	public String heartRate = "";
	public String electricity = "";// 电量

	public InHeartRateCmd(String heartRate, String electricity) {
		this.type = "14";
		this.heartRate = heartRate;
		this.electricity = electricity;
	}

	@Override
	public String toCmd() {
		return null;
	}

	@Override
	public void handle(IoSession session) {
		MQWatchMessage.sendData(this);
	}
}
