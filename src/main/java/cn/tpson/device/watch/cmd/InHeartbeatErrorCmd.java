package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

/**
 * （cmd：99）心跳异常报文
 * 
 * @author tangbin
 *
 */
public class InHeartbeatErrorCmd extends BaseCommand {
	public String value = "";// 位置

	public InHeartbeatErrorCmd(String value) {
		this.type = "99";
		this.value = value;
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
