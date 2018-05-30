/**
 * 
 */
package cn.tpson.device.watch.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 
 * @author jsper
 * @Date 2017-12-24
 * @Copyright 2017 Hangzhou Tpson technology .Inc All rights reserved.
 */
public class WatchProtocolFactory implements ProtocolCodecFactory {
	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;

	public WatchProtocolFactory() {
		// this.setDecoder(new WatchDecoder(Charset.forName("UTF-8"),"\r\n"));
		// //For Windows
		this.setDecoder(new WatchDecoder()); // For Windows
		this.setEncoder(new WatchEncoder());
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return this.decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return this.encoder;
	}

	public ProtocolDecoder getDecoder() {
		return decoder;
	}

	public void setDecoder(ProtocolDecoder decoder) {
		this.decoder = decoder;
	}

	public ProtocolEncoder getEncoder() {
		return encoder;
	}

	public void setEncoder(ProtocolEncoder encoder) {
		this.encoder = encoder;
	}
}
