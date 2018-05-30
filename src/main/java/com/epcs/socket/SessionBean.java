package com.epcs.socket;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;

import com.epcs.utils.parser2.CarInfo;

/**
 * Mulu
 */
@SuppressWarnings("serial")
public class SessionBean implements java.io.Serializable {

	public ByteArrayOutputStream dataCache = new ByteArrayOutputStream();
	private IoSession Session;
	private CarInfo lastCarInfo; // used to cal delta

	public boolean needUpdate = true;
	private List<Point2D.Double> fence = new ArrayList<Point2D.Double>();

	public List<Point2D.Double> getFence() {
		return fence;
	}

	public void setFence(List<Point2D.Double> fence) {
		this.fence = fence;
	}

	public IoSession getSession() {
		return Session;
	}

	public void setSession(IoSession session) {
		Session = session;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public CarInfo getLastCarInfo() {
		return lastCarInfo;
	}

	public void setLastCarInfo(CarInfo lastCarInfo) {
		this.lastCarInfo = lastCarInfo;
	}

}
