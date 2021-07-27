package com.zszxz.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.zszxz.rabbit.ConnectionUtil.getConnection;

/**
 * @author lsc
 * <p>应答机制 </p>
 */
public class Ack {

    public static void main(String[] args) {
        Ack ack = new Ack();
        ack.producer();
    }

    public  void producer(){
        Connection connection  = null;
        //获得信道
        Channel channel = null;
        try {
            connection = getConnection();
            channel = connection.createChannel();
            String queueName = "test-ack";
            String exchangeName = "test-ack";
            String routingKey = "test-router-ack";
            //声明交换器
            channel.exchangeDeclare(exchangeName, "direct", true);
            // 声明队列
            boolean durable = false;
            boolean exclusive = false;
            boolean autodelete = false;
            channel.queueDeclare(queueName, durable, exclusive, autodelete,null);
            // 绑定队列
            channel.queueBind(queueName,exchangeName,routingKey);
            //发布消息
            byte[] messageBodyBytes = "Hello Word !!!".getBytes();
            // 设置 消息属性
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.contentType("text/plain")
                    .priority(1)
                    .deliveryMode(2);
            // 设置信道为确认应答模式
            channel.confirmSelect();
            // 发送消息
            channel.basicPublish(exchangeName,routingKey,builder.build(),messageBodyBytes);
            try {
                if (!channel.waitForConfirms()){
                    System.out.println("发送消息失败");
                }else{
                    System.out.println("发送消息成功");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
