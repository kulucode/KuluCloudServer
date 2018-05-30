package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * 主动心率： 114
 * 
 * @author tangbin
 *
 */
public class OutHeartRateCmd extends BaseCommand {

	public OutHeartRateCmd() {
		this.type = "114";
	}

	@Override
	public String toCmd() {
		// @B#@,V01,114,@E#@
		return formatCmdStr(null);
	}

	@Override
	public void handle(IoSession session) {
	}
}
