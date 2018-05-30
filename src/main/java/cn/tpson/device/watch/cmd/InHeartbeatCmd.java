package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * （cmd：44）心跳报文
 * 
 * @author tangbin
 *
 */
public class InHeartbeatCmd extends BaseCommand {

	public InHeartbeatCmd() {
		this.type = "44";
	}

	@Override
	public String toCmd() {
		return null;
	}

	@Override
	public void handle(IoSession session) {
		session.write(new OutHeartbeatCmd());
	}
}
