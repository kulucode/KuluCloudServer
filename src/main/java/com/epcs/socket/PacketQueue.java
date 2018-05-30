package com.epcs.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketQueue {

	public BlockingQueue<SessionPacketBean> queue = new LinkedBlockingQueue<SessionPacketBean>();

	public void produce(SessionPacketBean packet) {
		// put方法放入一个packet，若basket满了，等到basket有位置
		try {
			queue.put(packet);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 消费SB,从篮子中取走
	public SessionPacketBean consume() {
		// take方法取出一个sb，若basket为空，等到basket有苹果为止(获取并移除此队列的头部)
		try {
			return queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
