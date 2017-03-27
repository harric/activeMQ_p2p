package com.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * MQ连接及
 * @author Vivi
 *
 */
public  class ConnUtils {

		//ConnectionFactory ：连接工厂，JMS 用它创建连接
		private static ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		private static Connection connection = null;
	    // Session： 一个发送或接收消息的线程
	    private static Session session;
	    // Destination ：消息的目的地;消息发送给谁.
	    private static Destination destination;
	    private static MessageConsumer consumer;
	    private static MessageProducer producer;
	    public final static long CREATE_TYPE_CONSUMER=1L; //消费者
	    public final static long CREATE_TYPE_PRODUCER=2L;//生产者
	    /**
	     * 
	     * @param QueueName 队列名
	     * @param type 生产者还是消费者
	     * @param user 用户名
	     * @param pass 密码
	     * @param Addr 连接MQ地址
	     * @return
	     * @throws JMSException
	     */
	    public static Object getConn(String QueueName,long type,String user,String pass,String addr) throws JMSException{
	    	
	    	//沒有給用戶名及密碼等信息為默認
	    	if((user==null||user.trim().equals(""))&&
	    			(pass==null||pass.trim().equals(""))&&(addr==null||addr.trim().equals(""))){
	    		//第一步，獲取连接工厂
	         	connectionFactory=new ActiveMQConnectionFactory(
	         			ActiveMQConnection.DEFAULT_USER, 
	         			ActiveMQConnection.DEFAULT_PASSWORD,
	         			"tcp://localhost:61616");
	    	}else{
	    		//第一步，獲取连接工厂
	         	connectionFactory=new ActiveMQConnectionFactory(user,pass,addr);
	    	}
	    	
         	//第二步 获取connection
 			connection =connectionFactory.createConnection();
 			//第三步，启动
 			connection.start();
 			//第四步，获取session，拿到操作连接,(是否支持事务)  
 			session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
 			//第五步  创建一个消息目标
 			destination = session.createQueue(QueueName);
 			if(type==CREATE_TYPE_CONSUMER){
 				consumer=session.createConsumer(destination);
 				return consumer;
 			}else if(type==CREATE_TYPE_PRODUCER){
 				// 得到消息生成者(sender)
 	            producer = session.createProducer(destination); 
// 	           static final int NON_PERSISTENT = 1;//不持久化：服务器重启之后，消息销毁 
//             static final int PERSISTENT = 2;//持久化：服务器重启之后，消息仍存在 
 	            //持久化
 	            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 	            return producer;
 			}else{
 				return null;
 			}
	    }
	    /**
	     * 获取session
	     * @return
	     */
	    public static Session getSession(){
	    	if(session==null){
	    		return null;
	    	}
	    	return session;
	    }
	    
	    
	    public static void connection_Close() throws JMSException{
	    	if(connection!=null){
	    		connection.close();
	    	}
	    }
	    public static void session_Close() throws JMSException{
	    	if(session!=null){
	    		session.close();
	    	}
	    	
	    }
}
