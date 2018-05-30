package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * 主动血压： 115
 * 
 * @author tangbin
 *
 */
public class OutBloodPressureCmd extends BaseCommand {

	public OutBloodPressureCmd() {
		this.type = "115";
	}

	@Override
	public String toCmd() {
		// @B#@,V01,115,@E#@
		return formatCmdStr(null);
	}

	@Override
	public void handle(IoSession session) {
	}
}
