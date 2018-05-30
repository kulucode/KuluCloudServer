package com.epcs.socket;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import com.epcs.utils.parser2.CarInfo;

/**
 * Created by Mulu on 2017/4/6.
 */
@ServerEndpoint("/websocket/push.ws")
public class WebSocketPushController {

	static Logger logger = Logger.getLogger(WebSocketPushController.class.getName());
	public static final Map<String, Session> clientMap = new HashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		clientMap.put(session.getId(), session);
	}

	@OnClose
	public void onClose(Session session) {
		try {
			session.close();
			clientMap.remove(session.getId());
			// System.out.println("remove: " + session.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnError
	public synchronized void error(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}

	@OnMessage
	public void onMessage(String requestJson, Session session) {
		System.out.println("message get: " + session.getId() + " " + requestJson);
	}

	// call by thead

	public static void pushToClients(CarInfo curCarInfo) {
		// TODO: 去掉数据库代码 by tangbin
		// 当有车的数据过来时，把车的数据发给客户端

		// CarBean carBean = new CarBean();
		// carBean.setCarId(curCarInfo.id);
		// carBean.setCurLong(curCarInfo.lonBaidu);
		// carBean.setCurLat(curCarInfo.latBaidu);
		// carBean.setAreAlerted(curCarInfo.areAlerted);
		//
		// if (clientMap.size() > 0) {
		// for (String key : clientMap.keySet()) {
		// Session session = clientMap.get(key);
		// if (session != null) {
		// try {session
		// logger.info("push to client :" + session.getId() + " with data:" +
		// JsonUtil.toJson(carBean));
		// .getBasicRemote().sendText(JsonUtil.toJson(carBean));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// // logger.error("push to client :" + session.getId()
		// // + " FAILED");
		// }
		// }
		//
		// }
		//
		// } else {
		// logger.info("No client exist,no push!");
		// }

	}
}
