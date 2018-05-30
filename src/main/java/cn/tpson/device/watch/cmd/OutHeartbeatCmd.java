package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * 心跳
 * 
 * @author tangbin
 *
 */
public class OutHeartbeatCmd extends BaseCommand {

	public OutHeartbeatCmd() {
		this.type = "21";
	}

	@Override
	public String toCmd() {
		// @B#@,V01,21,@E#@
		return formatCmdStr(null);
	}

	@Override
	public void handle(IoSession session) {
	}
}
