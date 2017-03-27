package com.mq.p2p;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

public class Sender2 {
    // MessageProducer：消息发送者  
   private static MessageProducer producer;
   
   
   public void producerSendMessage() throws JMSException{
	        
	   producer=(MessageProducer) ConnUtils.getConn("FirstMyQueue", ConnUtils.CREATE_TYPE_PRODUCER, null, null, null); 
	   
	   for (int i = 1; i <= 10; i++) {  
           TextMessage message = ConnUtils.getSession().createTextMessage("Hello MQ , I am vivid.this is my "  
                   + i+"th visited !");  
           // 发送消息到目的地方  
           System.out.println("发送消息：" + "MQ发送的消息" + i);  
           producer.send(message);  
       }  
   }
   
   public static void main(String[] args) throws JMSException {
	new Sender2().producerSendMessage();
}
}
