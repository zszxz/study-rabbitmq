//package com.zszxz.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author lsc
// * <p> </p>
// */
//@Configuration
//public class TopicConfig {
//
//    @Bean
//    public Queue firstQueue() {
//        return new Queue("topic-queue-1");
//    }
//
//    @Bean
//    public Queue secondQueue() {
//        return new Queue("topic-queue-2");
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("topic-ex");
//    }
//
//    @Bean
//    Binding bindingExchangeMessage1() {
//        return BindingBuilder.bind(firstQueue()).to(exchange()).with("topic-routing-1");
//    }
//
//    @Bean
//    Binding bindingExchangeMessage2() {
//        return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic-routing-2");
//    }
//}
