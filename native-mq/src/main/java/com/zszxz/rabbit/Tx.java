package com.zszxz.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.zszxz.rabbit.ConnectionUtil.getConnection;

/**
 * @author lsc
 * <p> 消息事物</p>
 */
public class Tx {

    public  void producer(){
        Connection connection  = null;
        //获得信道
        Channel channel = null;
        try {
            connection = getConnection();
            channel = connection.createChannel();
            String queueName = "test-tx";
            String exchangeName = "test-tx";
            String routingKey = "test-router-tx";
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
            try {
                // 设置信道为事物模式
                channel.txSelect();
                // 发送消息
                channel.basicPublish(exchangeName,routingKey,builder.build(),messageBodyBytes);
                // 提交事物
                channel.txCommit();

            }catch (Exception e){
                e.printStackTrace();
                // 回滚事物
                channel.txRollback();
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
