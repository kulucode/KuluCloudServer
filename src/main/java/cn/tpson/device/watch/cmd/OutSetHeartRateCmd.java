package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * 心率上传间隔设置： 45
 * 
 * @author tangbin
 *
 */
public class OutSetHeartRateCmd extends BaseCommand {

	// public int time = 30;// 0-300
	public int time = 1;// 0-300

	public OutSetHeartRateCmd(int time) {
		this.type = "45";
		this.time = time;
	}

	@Override
	public String toCmd() {
		// @B#@,V01,45,30,@E#@
		return formatCmdStr(time + "");
	}

	@Override
	public void handle(IoSession session) {
	}
}
