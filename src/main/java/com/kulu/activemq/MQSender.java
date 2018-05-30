package com.kulu.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.kulu.utils.JSONUtils;
import com.kulu.utils.PropertiesUtils;

public class MQSender {
	private static final int SEND_NUMBER = 5;
	private ConnectionFactory connectionFactory = null;
	public static MQSender instance = null;// 单例

	public static MQSender getInstance() {
		if (instance == null) {
			instance = new MQSender();
		}
		return instance;
	}

	public void start() {

	}

	// 发送车子的消息
	public void sendTruckservMessage(MQMessage message) throws Exception {
		sendMessage(MQReceiver.TOPIC_TRUCKSERV_TO_CLOUD, JSONUtils.toJSONString(message));
	}

	// 发送手环的消息
	public void sendWingservMessage(MQMessage message) throws Exception {
		sendMessage(MQReceiver.TOPIC_WINGSERV_TO_CLOUD, JSONUtils.toJSONString(message));
	}

	public void sendMessage(String queues, String text) {
		System.out.println(queues + ":" + text);
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// MessageProducer：消息发送者
		MessageProducer producer;
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				PropertiesUtils.config.get("mq_server") + ":" + PropertiesUtils.config.get("mq_port"));
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			destination = session.createQueue(queues);
			// 得到消息生成者【发送者】
			producer = session.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 构造消息，此处写死，项目就是参数，或者方法获取
			TextMessage message = session.createTextMessage(text);
			System.out.println("发送消息：[" + queues + "]:" + text);
			producer.send(message);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (Throwable ignore) {
			}
		}
	}
}