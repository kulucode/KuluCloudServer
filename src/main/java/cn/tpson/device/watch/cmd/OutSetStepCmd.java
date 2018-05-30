package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * 计步上传间隔设置： 46
 * 
 * @author tangbin
 *
 */
public class OutSetStepCmd extends BaseCommand {

	// public int time = 30;// 0-300
	public int time = 1;// 0-300

	public OutSetStepCmd(int time) {
		this.type = "46";
		this.time = time;
	}

	@Override
	public String toCmd() {
		// @B#@,V01,46,30,@E#@
		return formatCmdStr(time + "");
	}

	@Override
	public void handle(IoSession session) {
	}
}
