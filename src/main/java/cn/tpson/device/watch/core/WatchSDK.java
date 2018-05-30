/**
 * 手表SDK 主类
 */
package cn.tpson.device.watch.core;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import cn.tpson.device.watch.codec.WatchProtocolFactory;

/**
 * @author jsper xu
 * @Date 2017-12-24
 * @Copyright 2017 Hangzhou Tpson technology .Inc All rights reserved.
 */
public class WatchSDK {

	private int port = 2020;

	private IoAcceptor accepter = null;

	private WatchHandler minaHandler = null;
	private final String version = "v1.0";

	public WatchSDK() {
		minaHandler = new WatchHandler();
		accepter = new NioSocketAcceptor();
		accepter.setHandler(minaHandler);
	}

	/**
	 * SDK初始化
	 * 
	 * @param port
	 * @return
	 */
	public void init(int port) {
		this.port = port;
		// 检查端口是否被占用，框架的准备工作

		// accepter.getFilterChain().addLast("codec", new
		// ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"
		// ),"\r\n","\r\n")));
		accepter.getFilterChain().addLast("logger", new LoggingFilter());
		accepter.getFilterChain().addLast("codec", new ProtocolCodecFilter(new WatchProtocolFactory()));
	}

	/**
	 * 开启SDK
	 */
	public void start() {
		while (true) {
			try {
				accepter.bind(new InetSocketAddress(this.port));
				break;
			} catch (Exception e) {
				try {
					System.out.println(this.port + "端口被占用，1s后重试...");
					Thread.sleep(1000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 停止SDK工作
	 */
	public void stop() {
		// 包含清理资源的工作
		if (minaHandler != null) {
			minaHandler.stop();
		}

		if (accepter != null) {
			accepter.unbind();
			accepter.dispose(true);
		}

	}

	/**
	 * 获取SDK版本信息
	 * 
	 * @return
	 */
	public String getVersion() {
		return this.version;
	}

}
