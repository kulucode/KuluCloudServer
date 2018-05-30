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

/**
 * Created by Mulu on 2017/4/6.
 */
@ServerEndpoint("/websocket/data.ws")
public class WebSocketDataController {
	static Logger logger = Logger.getLogger(WebSocketDataController.class.getName());
	public static final Map<String, Session> clientMap = new HashMap<>();
	// static CarHisService service = new CarHisService();

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
		// save the data to database
		// TODO: 去掉数据库代码 by tangbin
		// CarBean carBean = updateDatabase(requestJson);
		//
		// // ***send to client
		// if (carBean != null)
		// pushToClients(carBean);
		//
		// }
		//
		// private CarBean updateDatabase(String requestJson) {
		//
		// // CarInfo carInfo =
		// PacketParser.parsePacket(requestJson.getBytes());
		//
		// // if (carInfo != null) {
		// // CarBean carBean = new CarBean();
		// // service.save(carInfo);
		// // carBean.setCarId(carInfo.id);
		// // carBean.setCurLong(carInfo.lon);
		// // carBean.setCurLat(carInfo.lat);
		// // return carBean;
		// // }
		// return null;
		//
		// }
		//
		// private void pushToClients(CarBean carBean) {
		//
		// Map<String, Session> clients = WebSocketPushController.clientMap;
		// if (clients.size() > 0) {
		// for (String key : clients.keySet()) {
		// Session session = clients.get(key);
		// if (session != null) {
		// try {
		// // logger.info(
		// // "push to client :" + session.getId() + " with
		// // data:" + JsonUtil.toJson(carBean));
		// session.getBasicRemote().sendText(JsonUtil.toJson(carBean));
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
		//
	}

}
