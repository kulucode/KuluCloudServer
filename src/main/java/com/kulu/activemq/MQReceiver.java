package com.kulu.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.alibaba.fastjson.JSONObject;
import com.kulu.utils.JSONUtils;
import com.kulu.utils.PropertiesUtils;

public class MQReceiver extends Thread {

	public static final String mq_prev = PropertiesUtils.config.get("mq_prev");
	public static final String TOPIC_CLOUD_TO_TRUCKSERV = mq_prev + "CLOUD_TO_TRUCKSERV";// 云服务到车载终端服务
	public static final String TOPIC_TRUCKSERV_TO_CLOUD = mq_prev + "TRUCKSERV_TO_CLOUD";// 车载终端服务到云服务
	public static final String TOPIC_CLOUD_TO_WINGSERV = mq_prev + "CLOUD_TO_WINGSERV";// 云服务到手表终端服务
	public static final String TOPIC_WINGSERV_TO_CLOUD = mq_prev + "WINGSERV_TO_CLOUD";// 手表终端服务到云服务

	private ConnectionFactory connectionFactory;
	// Connection ：JMS 客户端到JMS Provider 的连接
	private Connection connection = null;
	// Session： 一个发送或接收消息的线程
	private Session session;
	// Destination ：消息的目的地;消息发送给谁.
	private Destination destination;
	// 消费者，消息接收者
	private MessageConsumer consumer1;
	private MessageConsumer consumer3;

	public static MQReceiver instance = null;// 单例

	public static MQReceiver getInstance() {
		if (instance == null) {
			instance = new MQReceiver();
		}
		return instance;
	}

	public void run() {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		this.connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				PropertiesUtils.config.get("mq_server") + ":" + PropertiesUtils.config.get("mq_port"));
		try {
			// 构造从工厂得到连接对象
			this.connection = connectionFactory.createConnection();
			// 启动
			this.connection.start();
			// 获取操作连接
			this.session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
			// TOPIC_CLOUD_TO_TRUCKSERV
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			this.consumer1 = session.createConsumer(session.createQueue(TOPIC_CLOUD_TO_TRUCKSERV));
			this.consumer1.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					try {
						TextMessage message = (TextMessage) msg;
						System.out.println("车辆队列:" + message.getText());
						MQMessage obj = JSONUtils.ToObj(message.getText(), MQMessage.class);
						if ("reg_back".equals(obj.getTag())) {
							JSONObject ret = (JSONObject) obj.getDatabody();
							boolean isLogin = false;
							if (ret.containsKey("r")) {
								isLogin = 0 == (int) ret.get("r");
							}
							String authKey = ret.getString("token");
							MQCarMessage.regCallback(isLogin, obj.getEqpid(), authKey, obj.getIndata());
						} else if ("authKeyLogin_back".equals(obj.getTag())) {
							JSONObject ret = (JSONObject) obj.getDatabody();
							boolean isLogin = false;
							if (ret.containsKey("r")) {
								isLogin = 0 == (int) ret.get("r");
							}
							MQCarMessage.authKeyLoginCallback(isLogin, obj.getEqpid(), obj.getIndata());

						}
						System.out.println(obj);
						message.acknowledge();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}

			});
			// TOPIC_CLOUD_TO_WINGSERV
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			this.consumer3 = session.createConsumer(session.createQueue(TOPIC_CLOUD_TO_WINGSERV));
			this.consumer3.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					try {
						TextMessage message = (TextMessage) msg;
						System.out.println("手环队列:" + message.getText());
						MQMessage obj = JSONUtils.ToObj(message.getText(), MQMessage.class);
						if ("login_back".equals(obj.getTag())) {
							JSONObject ret = (JSONObject) obj.getDatabody();
							boolean isLogin = false;
							if (ret.containsKey("r")) {
								isLogin = 0 == (int) ret.get("r");
							}

							MQWatchMessage.loginCallback(isLogin, obj.getEqpid(), ret);
						}
						System.out.println(obj);
						message.acknowledge();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}

			});

		} catch (Exception e) {
			try {
				if (null != this.connection)
					this.connection.close();
			} catch (Throwable ignore) {
			}
			e.printStackTrace();
		}
	}
}