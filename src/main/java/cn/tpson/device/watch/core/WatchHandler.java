package cn.tpson.device.watch.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQReceiver;
import com.kulu.activemq.MQSender;
import com.kulu.activemq.MQWatchMessage;

import cn.tpson.device.watch.cmd.BaseCommand;
import cn.tpson.device.watch.cmd.InHeartbeatCmd;

public class WatchHandler extends IoHandlerAdapter {
	public static final long sleepTime = 1 * 30 * 1000;// 30秒检测一次
	public static final long outTime = 3 * 60 * 1000;// 3分钟
	public static final String LOGIN_KEY = "loginKey";
	public static final String LOCK = "LOCK";
	public static MQReceiver consumer = MQReceiver.getInstance();// 消费者，接收者
	public static MQSender producer = MQSender.getInstance();// 广播者，发送者

	public boolean isRun = true;

	public static HashMap<String, Long> userIds = new HashMap<String, Long>();// 在线列表id:user
	public static HashMap<String, IoSession> users = new HashMap<String, IoSession>();// 在线列表id:user
	public static HashMap<IoSession, Date> heartbeatList = new HashMap<IoSession, Date>();// 心跳用户列表

	public WatchHandler() {
		init();
	}

	public void init() {
		consumer.start();
		producer.start();
		// 处理心跳
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRun) {
					try {
						Thread.sleep(sleepTime);// 1分钟
						synchronized (LOCK) {
							Date now = new Date();
							for (IoSession session : heartbeatList.keySet()) {
								if (now.getTime() - heartbeatList.get(session).getTime() > outTime) {
									session.close(true);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void stop() {
		isRun = false;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof List) {
			@SuppressWarnings("unchecked")
			List<BaseCommand> list = (List<BaseCommand>) message;
			Object key = session.getAttribute(LOGIN_KEY);
			if (key == null && list.size() > 0) {
				login(session, list.get(0));
			}
			for (BaseCommand cmd : list) {
				if (cmd.getClass() == InHeartbeatCmd.class) {// 心跳消息
					synchronized (LOCK) {
						heartbeatList.put(session, new Date());// 更新时间
					}
				}
				cmd.handle(session);
			}
		}
	}

	public void login(IoSession session, BaseCommand cmd) throws Exception {
		String loginKey = cmd.getToken();
		MQWatchMessage.login(loginKey);
		userIds.put(loginKey, session.getId());
		if (users.containsKey(loginKey)) {
			IoSession oldSession = users.get(loginKey);
			if (oldSession.getId() != session.getId()) {// 不是同一个会话
				oldSession.close(true);
			}
		}
		users.put(loginKey, session);
		session.setAttribute(LOGIN_KEY, loginKey);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session Colsed.");
		Object key = session.getAttribute(LOGIN_KEY);
		if (key != null) {
			String _key = (String) key;
			if (userIds.get(_key) == session.getId()) {
				userIds.remove(_key);
				users.remove(_key);
				MQWatchMessage.loginOut(_key);
			}
		}
		synchronized (LOCK) {
			heartbeatList.remove(session);
		}
		super.sessionClosed(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		synchronized (LOCK) {
			heartbeatList.put(session, new Date());// 刚刚创建
		}
		System.out.println("Session Created.");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}

}
