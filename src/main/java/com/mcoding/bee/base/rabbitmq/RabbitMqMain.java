package com.mcoding.bee.base.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.stream.IntStream;

/**
 * @author wzt on 2020/5/19.
 * @version 1.0
 */
public class RabbitMqMain {

    private static ConnectionFactory factory = new ConnectionFactory();

    static {
        factory.setUsername("radmin");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        factory.setHost("47.95.192.230");
        factory.setPort(5672);
    }


    public static void main(String[] args) {


        IntStream.rangeClosed(1, 100).forEach(count -> {

            try {
                sendMsg(String.format("时间 => %s", System.currentTimeMillis()));
                System.out.println(count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    private static void sendMsg(String message) throws Exception {
        Connection conn = factory.newConnection();

        // 创建信道
        Channel channel = conn.createChannel();

        // 声明队列
        channel.queueDeclare("trade-order-queue", true, false, false, null);

        try {
            channel.txSelect();

            // 发送消息
            channel.basicPublish("exc-01", "trade-order", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            channel.txCommit();
        } catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        } finally {
            channel.close();
            conn.close();
        }
    }

}
