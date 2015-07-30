package cn.studease.util.mom.lesson5;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsTopicForCritical {

    private static final String EXCHANGE_NAME = "topic_logs_rabbit";

    public static void main(String[] argv) throws Exception {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.204");
        factory.setPort(5672);
        factory.setUsername("dengjie");
        factory.setPassword("dengjie");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 随机生成一个队列
        String queueName = channel.queueDeclare().getQueue();

        // 接收所有与kernel相关的消息
        channel.queueBind(queueName, EXCHANGE_NAME, "*.critical");

        System.out
                .println(" [*] Waiting for critical messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            String routingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println(" [x] Received routingKey = " + routingKey
                    + ",msg = " + message + ".");
        }
    }
}
