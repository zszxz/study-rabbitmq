package com.zszxz.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static com.zszxz.rabbit.ConnectionUtil.getConnection;

/**
 * @author lsc
 * <p> 消息过期</p>
 */
public class TTL {
    public static void main(String[] args) throws IOException {
        TTL tt = new TTL();
        tt.messageTTLproducer();
        tt.queueTTLproducer();
    }

    public  void queueTTLproducer(){
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-expire";
            String exchangeName = "test-ex";
            String routingKey = "test-router";
            //声明交换器
            channel.exchangeDeclare(exchangeName, "direct", true);
            HashMap<String, Object> args = new HashMap<>();
            // 设置队列过期时间
            args.put("x-expires",1000*60*3);
            // 声明队列
            boolean durable = false;
            boolean exclusive = false;
            boolean autodelete = false;
            channel.queueDeclare(queueName, durable, exclusive, autodelete, args);
            // 绑定队列
            channel.queueBind(queueName,exchangeName,routingKey);
            //发布消息
            byte[] messageBodyBytes = "Hello Word !!!".getBytes();
            // 设置 消息属性
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.contentType("text/plain")
                    .priority(1)
                    .deliveryMode(2);

            channel.basicPublish(exchangeName,routingKey,builder.build(),messageBodyBytes);
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public  void messageTTLproducer(){
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-undurable";
            String exchangeName = "test-ex";
            String routingKey = "test-router";
            //声明交换器
            channel.exchangeDeclare(exchangeName, "direct", true);
            HashMap<String, Object> args = new HashMap<>();
            // 设置消息
            args.put("x-message-ttl",8000);
            // 声明队列
            boolean durable = false;
            boolean exclusive = false;
            boolean autodelete = false;
            channel.queueDeclare(queueName, durable, exclusive, autodelete, args);
            // 绑定队列
            channel.queueBind(queueName,exchangeName,routingKey);
            //发布消息
            byte[] messageBodyBytes = "Hello Word !!!".getBytes();
            // 设置 消息属性
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.contentType("text/plain")
                    .priority(1)
                    .deliveryMode(2);

            channel.basicPublish(exchangeName,routingKey,builder.build(),messageBodyBytes);
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
