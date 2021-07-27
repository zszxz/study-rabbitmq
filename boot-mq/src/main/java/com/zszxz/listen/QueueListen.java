package com.zszxz.listen;

import com.rabbitmq.client.Channel;
import com.zszxz.constant.MqConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lsc
 * <p>消费者--消息监听--自动应答 </p>
 */
//@Component
//@RabbitListener(queues = MqConstant.QUEUE_NAME)//监听的队列名称
//public class QueueListen {
//
//
//    @RabbitHandler
//    public void process(Message message, Channel channel) {
//        System.out.println("消费者收到消息-----------" + new String(message.getBody()));
//    }
//}
