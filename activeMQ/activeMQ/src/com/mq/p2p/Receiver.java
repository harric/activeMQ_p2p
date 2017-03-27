package com.mq.p2p;

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

/**
 * P2p模式-点对点 -消费者
 * @author Vivi
 */
public class Receiver implements MessageListener{
	
	//ConnectionFactory ：连接工厂，JMS 用它创建连接
	private static ConnectionFactory connectionFactory;
	// Connection ：JMS 客户端到JMS Provider 的连接
	private static Connection connection = null;
    // Session： 一个发送或接收消息的线程
    private static Session session;
    // Destination ：消息的目的地;消息发送给谁.
    private static Destination destination;
    private static MessageConsumer consumer;
    
    public void receive() throws Exception{
    	
    	 try {
     		//第一步，獲取连接工厂
         	connectionFactory=new ActiveMQConnectionFactory(
         			ActiveMQConnection.DEFAULT_USER, 
         			ActiveMQConnection.DEFAULT_PASSWORD,
         			"tcp://localhost:61616");
         	//第二步 获取connection
 			connection =connectionFactory.createConnection();
 			//第三步，启动
 			connection.start();
 			//第四步，获取session，拿到操作连接,(是否支持事务)  
 			session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
 			//第五步  创建一个消息目标
 			destination = session.createQueue("FirstMyQueue");
 			//第六步 
            consumer = session.createConsumer(destination);
             
            //第七步，设置监听
//            consumer.setMessageListener(this);
            
            //此处也可以用 consumer.receive(time)，不用实现MessageListener
            while (true) {
                //设置接收者接收消息的时间
                TextMessage message = (TextMessage) consumer.receive(10000);
                if (null != message) {
                    System.out.println("接收到消息：" + message.getText());
                } else {
                    break;
                }
            }
             
 		} catch (JMSException e) {
 			e.printStackTrace();
 		}finally {
 			try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
		}

    }
   
	@Override
	public void onMessage(Message message) {
		 //如果消息是TextMessage  
        if (message instanceof TextMessage) {  
            //强制转换一下  
            TextMessage txtMsg = (TextMessage) message;  
            try {  
                //输出接收到的消息  
                System.out.println("接收到消息 " + txtMsg.getText());  
            } catch (JMSException e) {  
                e.printStackTrace();  
            }  
        }  
	}
    public static void main(String[] args) throws Exception {
		new Receiver().receive();
	}
}
