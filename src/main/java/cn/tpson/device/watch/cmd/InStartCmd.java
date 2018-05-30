package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * （cmd：2）开机通告
 * 
 * @author tangbin
 *
 */
public class InStartCmd extends BaseCommand {
	public String electricity = "";// 电量

	public InStartCmd(String electricity) {
		this.type = "2";
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
