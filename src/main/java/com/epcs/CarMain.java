package com.epcs;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.epcs.socket.PacketQueue;
import com.epcs.socket.PacketQueueConsumer;
import com.epcs.socket.SocketHandler;
import com.epcs.socket.WebSocketCarInfoConsumer;
import com.epcs.socket.WebSocketCarInfoQueue;
import com.epcs.utils.ThreadKeeper;
import com.kulu.utils.PropertiesUtils;

public class CarMain {
	private static final int port = PropertiesUtils.config.getInteger("truckserv_port");
	private static final Logger logger = Logger.getLogger(CarMain.class);
	public static boolean isDebug;
	public PacketQueueConsumer mConsumer;
	public Thread mPacketConsumerThread;
	public WebSocketCarInfoConsumer mCarInfoConsumer;
	public Thread mWebSocketCarInfoConsumerThread;
	private NioSocketAcceptor mAcceptor;
	private ThreadKeeper mKeeperRunnable;
	private Thread mKeeperThread;

	public void start() {
		System.out.println("CarServer port:" + port);
		PacketQueue packetQueue = new PacketQueue();
		try {
			mAcceptor = new NioSocketAcceptor();
			mAcceptor.setReuseAddress(true);
			// set config
			mAcceptor.getSessionConfig().setReceiveBufferSize(10240); // 设置接收缓冲区的大小
			mAcceptor.getSessionConfig().setMinReadBufferSize(1024);
			mAcceptor.getSessionConfig().setSendBufferSize(10240);// 设置输出缓冲区的大小
			mAcceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 50000);// 读(接收通道)空闲时间:40秒
			mAcceptor.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, 50000);// 写(发送通道)空闲时间:50秒
			mAcceptor.setHandler(new SocketHandler(packetQueue));
			while (true) {
				try {
					mAcceptor.bind(new InetSocketAddress(port));
					break;
				} catch (Exception e) {
					try {
						System.out.println(port + "端口被占用，1s后重试...");
						Thread.sleep(1000);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebSocketCarInfoQueue carInfoQueue = new WebSocketCarInfoQueue();
		// ***start the consumer thread
		mConsumer = new PacketQueueConsumer(packetQueue, carInfoQueue);
		mPacketConsumerThread = new Thread(mConsumer);
		mPacketConsumerThread.start();

		// *** start the websocket push thead

		mCarInfoConsumer = new WebSocketCarInfoConsumer(carInfoQueue);
		mWebSocketCarInfoConsumerThread = new Thread(mCarInfoConsumer);
		mWebSocketCarInfoConsumerThread.start();

		// *** start the thread keeper
		mKeeperRunnable = new ThreadKeeper();
		mKeeperRunnable.register(mPacketConsumerThread, mConsumer);
		mKeeperRunnable.register(mWebSocketCarInfoConsumerThread, mCarInfoConsumer);
		mKeeperThread = new Thread(mKeeperRunnable);
		mKeeperThread.start();
	}

	public static void main(String[] main) {
		// WatchMain.SocketPortInit(port);
		new CarMain().start();
		System.out.println("CarServer start ok");
	}
}
