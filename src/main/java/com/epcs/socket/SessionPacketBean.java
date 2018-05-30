package com.epcs.socket;

import org.apache.mina.core.session.IoSession;

import com.epcs.utils.parser2.CommPacket;

/**
 * Mulu
 */
@SuppressWarnings("serial")
public class SessionPacketBean implements java.io.Serializable {

	private IoSession session; 
	private CommPacket curCommPacket;

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	} 

	public CommPacket getCurCommPacket() {
		return curCommPacket;
	}

	public void setCurCommPacket(CommPacket curCommPacket) {
		this.curCommPacket = curCommPacket;
	}

}
