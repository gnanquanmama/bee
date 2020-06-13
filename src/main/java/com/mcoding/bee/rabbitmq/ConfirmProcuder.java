package com.mcoding.bee.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wzt on 2020/5/19.
 * @version 1.0
 */
@Slf4j
public class ConfirmProcuder {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setUsername(RabbitMQCommon.USER_NAME);
        connectionFactory.setPassword(RabbitMQCommon.PASSWORD);

        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.confirmSelect();

        long currentTime = System.currentTimeMillis();
        String msg = "Hello RabbitMQ Consumer Message";
        for (int i = 0; i < 1000_0000; i++) {
            log.info("生产端发送：{}", msg + i);

            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).build();
            channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTING_KEY, true, basicProperties, (msg + i).getBytes());
        }

        log.info("cost time {}", System.currentTimeMillis() - currentTime);

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.info("未确认消息，标识：" + deliveryTag);
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                log.info(String.format("已确认消息，标识：%d，多个消息：%b", deliveryTag, multiple));
            }
        });

    }

}
