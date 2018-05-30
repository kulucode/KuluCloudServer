package com.epcs.socket;

import org.apache.log4j.Logger;

import com.epcs.utils.parser2.CarInfo;

public class WebSocketCarInfoConsumer extends BaseRunnable {
	static Logger logger = Logger.getLogger(WebSocketCarInfoConsumer.class.getName());
	private WebSocketCarInfoQueue queue;

	public WebSocketCarInfoConsumer(WebSocketCarInfoQueue queue) {
		this.queue = queue;
	}

	public void run() {
		setShutdownRequested(false);
		while (!isShutdownRequested()) {

			logger.info("!!!!!!![WebSocketCarInfoConsumer] waiting for carinfo! Queue size:" + queue.queue.size());
			CarInfo carInfo = queue.consume();
			if (carInfo != null) {
				WebSocketPushController.pushToClients(carInfo);
			}
		}
	}

}
