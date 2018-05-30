package com.epcs.socket;

import org.apache.log4j.Logger;

public abstract class BaseRunnable implements Runnable {
	static Logger logger = Logger.getLogger(BaseRunnable.class.getName());

	private volatile boolean shutdownRequested = false;

	public BaseRunnable() {

	}

	public void shutdownRequest() {
		this.shutdownRequested = true;
	}

	public boolean isShutdownRequested() {
		return shutdownRequested;
	}

	public void setShutdownRequested(boolean shutdownRequested) {
		this.shutdownRequested = shutdownRequested;
	}

}
