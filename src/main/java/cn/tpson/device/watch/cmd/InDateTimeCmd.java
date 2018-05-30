package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * （cmd：1）时间同步报文
 * 
 * @author tangbin
 *
 */
public class InDateTimeCmd extends BaseCommand {

	public InDateTimeCmd() {
		this.type = "1";
	}

	@Override
	public String toCmd() {
		return null;
	}

	@Override
	public void handle(IoSession session) {
		session.write(new OutDateTimeCmd());
	}
}
