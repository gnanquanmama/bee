package com.mcoding.bee.base.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wzt on 2020/5/19.
 * @version 1.0
 */
@Slf4j
public class TxProcuder {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setUsername(RabbitMQCommon.USER_NAME);
        connectionFactory.setPassword(RabbitMQCommon.PASSWORD);

        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

        Connection connection = connectionFactory.newConnection();
        connection.isOpen();
        Channel channel = connection.createChannel();

        long currentTime = System.currentTimeMillis();
        String msg = "Hello RabbitMQ Consumer Message";
        for (int i = 0; i < 100; i++) {

            channel.txSelect();
            try {
                log.info("生产端发送：{}", msg + i);
                channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTING_KEY, true, null, (msg + i).getBytes());

            } catch (Exception e) {
                e.printStackTrace();
                channel.txRollback();
            } finally {
                channel.txCommit();
            }

        }

        log.info("cost time {}", System.currentTimeMillis() - currentTime);

    }

}
