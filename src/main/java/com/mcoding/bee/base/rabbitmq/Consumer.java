package com.mcoding.bee.base.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wzt on 2020/5/19.
 * @version 1.0
 */
@Slf4j
public class Consumer {


    public static final String EXCHANGE_NAME = "test_consumer_exchange";
    public static final String EXCHANGE_TYPE = "topic";
    public static final String ROUTING_KEY_TYPE = "consumer.#";
    public static final String ROUTING_KEY = "consumer.save";
    public static final String QUEUE_NAME = "test_consumer_queue";

    public static void main(String[] args) throws Exception {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root123");
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        //2 获取C onnection
        Connection connection = connectionFactory.newConnection();
        //3 通过Connection创建一个新的Channel
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_TYPE);

        //使用自定义消费者
        channel.basicConsume(QUEUE_NAME, true, new MyConsumer(channel));
        log.info("消费端启动成功");
    }
}