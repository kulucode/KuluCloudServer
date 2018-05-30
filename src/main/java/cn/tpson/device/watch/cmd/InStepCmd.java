package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * （cmd：13）计步周期上传
 * 
 * @author tangbin
 *
 */
public class InStepCmd extends BaseCommand {
	public String step = "";// 电量

	public InStepCmd(String step) {
		this.type = "13";
		this.step = step;
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
