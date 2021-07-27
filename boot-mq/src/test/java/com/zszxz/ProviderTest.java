package com.zszxz;

import com.rabbitmq.client.ConfirmCallback;
import com.zszxz.constant.MqConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lsc
 * <p> </p>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProviderTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSend(){
        String message = "zs hello word";
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(MqConstant.EXCHANGE_NAME,MqConstant.ROUTING_NAME,message,correlationId);
    }


}
