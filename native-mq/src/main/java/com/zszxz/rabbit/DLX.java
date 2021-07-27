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
 * <p> 死信队列 </p>
 */
public class DLX {

    public static void main(String[] args) {
        DLX dlx = new DLX();
//        dlx.dlxproducer();
        dlx.ttLproducer();
    }

    public  void dlxproducer(){
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-dlx";
            String exchangeName = "test-ex-dlx";
            String routingKey = "test-router-exlx";
            //声明交换器
            channel.exchangeDeclare(exchangeName, "direct", true);
            HashMap<String, Object> args = new HashMap<>();
            // 指定dlx
            args.put("x-dead-letter-exchange",exchangeName);
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

    public  void ttLproducer(){
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-ttl";
            String exchangeName = "test-ex-ttl";
            String routingKey = "test-router-ttl";
            //声明交换器
            channel.exchangeDeclare(exchangeName, "direct", true);
            HashMap<String, Object> args = new HashMap<>();
            // 指定dlx
            args.put("x-dead-letter-exchange","test-ex-dlx");
            // 指定dlx 路由键
            args.put("x-dead-letter-routing-key","test-router-exlx");
            // 设置消息过期时间
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
