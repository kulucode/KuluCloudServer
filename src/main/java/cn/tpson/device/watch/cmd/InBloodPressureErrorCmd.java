package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * 血压异常
 * 
 * @author tangbin
 *
 */
public class InBloodPressureErrorCmd extends BaseCommand {
	public String high = "";
	public String low = "";

	public InBloodPressureErrorCmd(String high, String low) {
		this.type = "113";
		this.high = high;
		this.low = low;
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
