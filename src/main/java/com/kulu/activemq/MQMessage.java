package com.kulu.activemq;

import java.util.Date;
import java.util.HashMap;

import com.kulu.utils.DateUtil;

public class MQMessage {
	private String eqpid = "";
	private String date = DateUtil.ToDateTimeString(new Date());
	private String tag = "";
	private Object databody = null;
	private HashMap<String, Object> indata = null;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getEqpid() {
		return eqpid;
	}

	public void setEqpid(String eqpid) {
		this.eqpid = eqpid;
	}

	public Object getDatabody() {
		return databody;
	}

	public void setDatabody(Object databody) {
		this.databody = databody;
	}

	public HashMap<String, Object> getIndata() {
		return indata;
	}

	public void setIndata(HashMap<String, Object> indata) {
		this.indata = indata;
	}

}
