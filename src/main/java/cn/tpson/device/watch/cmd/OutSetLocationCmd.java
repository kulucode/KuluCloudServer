package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * （cmd：19）位置数据上报周期设置
 * 
 * @author tangbin
 *
 */
public class OutSetLocationCmd extends BaseCommand {

	public int time = 1;// 0-300分钟

	public OutSetLocationCmd(int time) {
		this.type = "19";
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
