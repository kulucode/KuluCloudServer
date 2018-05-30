package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * （cmd：47）心率阀值设置
 * 
 * @author tangbin
 *
 */
public class OutSetHeartRateRangeCmd extends BaseCommand {

	public int low = 112;
	public int high = 200;

	public OutSetHeartRateRangeCmd(int high, int low) {
		this.type = "47";
		this.high = high;
		this.low = low;
	}

	@Override
	public String toCmd() {
		// @B#@,V01,47,35,165,@E#@
		return formatCmdStr(low + "," + high);
	}

	@Override
	public void handle(IoSession session) {
	}
}
