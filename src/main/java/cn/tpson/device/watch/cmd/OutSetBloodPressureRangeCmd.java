package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * （cmd：112）血压校准参数设置
 * 
 * @author tangbin
 *
 */
public class OutSetBloodPressureRangeCmd extends BaseCommand {

	public int low = 35;
	public int high = 165;

	public OutSetBloodPressureRangeCmd(int high, int low) {
		this.type = "112";
		this.high = high;
		this.low = low;
	}

	@Override
	public String toCmd() {
		// @B#@,V01,112,120,70,@E#@
		return formatCmdStr(high + "," + low);
	}

	@Override
	public void handle(IoSession session) {
	}
}
