package com.zszxz.config;

import com.zszxz.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsc
 * <p> </p>
 */
@Configuration
public class DirectConfig {
    @Bean
    public Queue directQueue() {
        return new Queue(MqConstant.QUEUE_NAME,true,false,false);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(MqConstant.EXCHANGE_NAME,true,false);
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(MqConstant.ROUTING_NAME);
    }

}
