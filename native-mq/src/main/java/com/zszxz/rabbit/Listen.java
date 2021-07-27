package com.zszxz.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.zszxz.rabbit.ConnectionUtil.getConnection;

/**
 * @author lsc
 * <p> 消息监听 </p>
 */
public class Listen {
    public static void main(String[] args) throws IOException {
        Listen back = new Listen();
        back.producer();
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

            channel.basicPublish(exchangeName,routingKey,true,builder.build(),messageBodyBytes);

            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                    String msg = new String(bytes);
                    System.out.println("返回消息："+msg);

                }
            });
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
