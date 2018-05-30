package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * （cmd：110）血压正常报文
 * 
 * @author tangbin
 *
 */
public class InBloodPressureOkCmd extends BaseCommand {
	public String high = "";
	public String low = "";

	public InBloodPressureOkCmd(String high, String low) {
		this.type = "110";
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
