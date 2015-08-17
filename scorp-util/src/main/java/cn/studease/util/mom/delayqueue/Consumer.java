package cn.studease.util.mom.delayqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Consumer {

    private static final String TASK_QUEUE_NAME = "MAIN_QUEUE";

    public static void main(String[] argv)
            throws java.io.IOException,
            InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.204");
        factory.setPort(5672);
        factory.setUsername("dengjie");
        factory.setPassword("dengjie");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(TASK_QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            // 1439792384602
            System.out.println(String.format(" [x] Received %s %s", message, System.currentTimeMillis()));
            doWork(message);
            System.out.println(" [x] Done");

        }
    }

    private static void doWork(String message) {
        System.out.println("save to db, message: " + message);
    }
}