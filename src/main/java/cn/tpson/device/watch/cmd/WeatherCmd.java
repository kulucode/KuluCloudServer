package cn.tpson.device.watch.cmd;

import org.apache.mina.core.session.IoSession;

public class WeatherCmd extends BaseCommand {

	public void handle(IoSession session) {

	}

	@Override
	public String toCmd() {
		return "";
	}

}
