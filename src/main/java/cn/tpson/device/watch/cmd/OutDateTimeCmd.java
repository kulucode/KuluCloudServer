package cn.tpson.device.watch.cmd;

import java.util.Date;

import org.apache.mina.core.session.IoSession;

import com.kulu.utils.DateUtil;

/**
 * 时间同步
 * 
 * @author tangbin
 *
 */
public class OutDateTimeCmd extends BaseCommand {

	public OutDateTimeCmd() {
		this.type = "38";
	}

	@Override
	public String toCmd() {
		// @B#@,V01,38,20150313180820,@E#@
		return formatCmdStr(DateUtil.ToString(new Date(), "yyyyMMddHHmmss"));
	}

	@Override
	public void handle(IoSession session) {
	}
}
