package com.zszxz.listen;

import com.rabbitmq.client.Channel;
import com.zszxz.constant.MqConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lsc
 * <p>消费者--手动应答成功 </p>
 */
@Component
public class AckQueueListen {

    @RabbitListener(queues = MqConstant.QUEUE_NAME)
    public void process1(Message message, Channel channel) {
        System.out.println("消费者接收消息: " + new String(message.getBody()));
        try {
            // 手动应答成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
