package com.zszxz.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author lsc
 * <p> 链接工具类 修改 地址 账号，密码可进行测试 </p>
 */
public class ConnectionUtil {
    // 获取连接
    public static Connection getConnection() throws IOException {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //定义连接地址
        factory.setHost("");
        //定义端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("test");
        factory.setUsername("");
        factory.setPassword("");
        // 通过工厂获取连接
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-ch";
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // 拉取模式消费
    public void pullConsumer(){
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-ch";
            // 获取响应
            GetResponse response = channel.basicGet(queueName, false);
            System.out.println(new String(response.getBody()));
            //
            channel.basicAck(response.getEnvelope().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 推送模式消费
    public void pushConsumer(){
        try {
            Connection connection = getConnection();
            boolean autoAck = false;
            String queueName = "test-ch";
            String tagName = "consume-tag";
            //获得信道
            Channel channel = connection.createChannel();
            channel.basicConsume(queueName,autoAck,tagName,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    System.out.println("键："+routingKey+"-----"+new String(body));
                    long deliveryTag = envelope.getDeliveryTag();
                    // 应答
                    channel.basicAck(deliveryTag,false);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void producer(){
        try {
            Connection connection = getConnection();
            //获得信道
            Channel channel = connection.createChannel();
            String queueName = "test-ch";
            String exchangeName = "test-ex";
            String routingKey = "test-router";
            //声明交换器
            channel.exchangeDeclare(exchangeName, "direct", true);
            // 声明队列
            channel.queueDeclare(queueName, true, false, false, null);
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
