package com.zszxz.rabbit;

/**
 * @author lsc
 * <p> 消费者 </p>
 */
public class Consumer {

    public static void main(String[] args) {
        ConnectionUtil connectionUtil = new ConnectionUtil();
        connectionUtil.pushConsumer();
    }
}
