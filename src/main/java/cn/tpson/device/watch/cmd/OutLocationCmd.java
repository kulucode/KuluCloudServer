package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * 主动定位： 25
 * 
 * @author tangbin
 *
 */
public class OutLocationCmd extends BaseCommand {

	public OutLocationCmd() {
		this.type = "25";
	}

	@Override
	public String toCmd() {
		// @B#@,V01,25,@E#@
		return formatCmdStr(null);
	}

	@Override
	public void handle(IoSession session) {
	}
}
