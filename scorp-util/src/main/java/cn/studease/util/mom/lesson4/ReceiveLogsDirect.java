package cn.studease.util.mom.lesson4;


import java.util.Random;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "ex_logs_direct";
    private static final String[] SEVERITIES = {"info", "warning", "error"};

    public static void main(String[] argv) throws java.io.IOException,
            java.lang.InterruptedException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.204");
        factory.setPort(5672);
        factory.setUsername("dengjie");
        factory.setPassword("dengjie");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明direct类型转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queueName = channel.queueDeclare().getQueue();
        String severity = getSeverity();
        // 指定binding_key
        channel.queueBind(queueName, EXCHANGE_NAME, severity);
        System.out.println(" [*] Waiting for " + severity + " logs. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received '" + message + "'");
        }
    }

    /**
     * 随机产生一种日志类型
     *
     * @return
     */
    private static String getSeverity() {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        return SEVERITIES[ranVal];
    }
}