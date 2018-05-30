package com.epcs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.kulu.utils.PropertiesUtils;

import cn.tpson.device.watch.core.WatchSDK;

public class WatchMain {
	private static final int port = PropertiesUtils.config.getInteger("wingserv_port");

	public static void SocketPortInit(int port) {
		Socket socket = null;
		while (true) {
			try {
				InetAddress Address = InetAddress.getByName("127.0.0.1");
				socket = new Socket(Address, port);
				try {
					System.out.println(port + "端口被占用，1s后重试...");
					Thread.sleep(1000);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				break;
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void start() {
		System.out.println("WatchServer port:" + port);
		// SocketPortInit(port);
		WatchSDK sdk = new WatchSDK();
		sdk.init(port);
		sdk.start();
	}

	public static void main(String[] args) {
		new WatchMain().start();
		System.out.println("WatchServer start ok");
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// WatchTestMain.main(null);
		// }
		// }).start();
	}
}
