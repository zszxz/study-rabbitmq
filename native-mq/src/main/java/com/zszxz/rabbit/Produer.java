package com.zszxz.rabbit;

/**
 * @author lsc
 * <p> 生产者</p>
 */
public class Produer {

    public static void main(String[] args) {
        ConnectionUtil connectionUtil = new ConnectionUtil();
        connectionUtil.producer();
    }

}
