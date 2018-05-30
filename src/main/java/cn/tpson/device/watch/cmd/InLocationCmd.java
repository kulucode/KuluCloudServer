package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * （cmd：6）轨迹报文(GPS)
 * 
 * @author tangbin
 *
 */
public class InLocationCmd extends BaseCommand {
	public String location = "";// 位置

	public InLocationCmd(String location) {
		this.type = "6";
		this.location = location;
	}

	@Override
	public String toCmd() {
		// @B#@,V01,25,@E#@
		return formatCmdStr(null);
	}

	@Override
	public void handle(IoSession session) {
		MQWatchMessage.sendData(this);
	}
}
