package com.mq.p2p;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

/**
 * P2p模式-点对点 -消费者
 * @author Vivi
 */
public class Receiver2{
	
    private static MessageConsumer consumer;
    
    public void receive() throws Exception{
    	
    	 try {
    		 consumer= (MessageConsumer) ConnUtils.getConn("FirstMyQueue", ConnUtils.CREATE_TYPE_CONSUMER, null, null, null);
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
				ConnUtils.connection_Close();
				ConnUtils.session_Close();
		}
	}
    public static void main(String[] args) throws Exception {
		new Receiver2().receive();
	}
}
