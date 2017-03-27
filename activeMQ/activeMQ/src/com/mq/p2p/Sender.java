package com.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	 // ConnectionFactory ：连接工厂，JMS 用它创建连接  
   private static ConnectionFactory connectionFactory; 
   // Connection ：JMS 客户端到JMS  
   private static Connection connection = null; 
    // Session： 一个发送或接收消息的线程  
   private static Session session; 
    // Destination ：消息的目的地;消息发送给谁.  
   private static Destination destination; 
    // MessageProducer：消息发送者  
   private static MessageProducer producer;
   
   
   public void producerSendMessage(){
	   // 构造ConnectionFactory实例对象
	   connectionFactory= new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, 
			   ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
	    try { // 构造从工厂得到连接对象  
	        connection = connectionFactory.createConnection();  
	        // 启动  
	        connection.start();  
	        // 获取操作连接  ,并开启事物
	        session = connection.createSession(Boolean.TRUE,  
	                Session.AUTO_ACKNOWLEDGE);  
	        destination = session.createQueue("FirstMyQueue");  
	        // 得到消息生成者
	        producer = session.createProducer(destination);  
	        // 设置不持久化
	        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);  
	        
	        //测试方式一
//	        TextMessage message = session.createTextMessage("Hello MQ ! I am vivid");  
//	        producer.send(message);
	        //测试方式二
	        sendMessage(session, producer);  
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
   
   public static void sendMessage(Session session, MessageProducer producer)  
           throws Exception {  
       for (int i = 1; i <= 10; i++) {  
           TextMessage message = session.createTextMessage("Hello MQ , I am vivid.this is my "  
                   + i+"th visited !");  
           // 发送消息到目的地方  
           System.out.println("发送消息：" + "MQ发送的消息" + i);  
           producer.send(message);  
       }  
   }  
   
   public static void main(String[] args) {
	new Sender().producerSendMessage();
}
}
