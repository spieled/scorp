package cn.studease.util.mom.delayqueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.HashMap;
import org.springframework.util.SerializationUtils;

/**
 * Author: liushaoping
 * Date: 2015/8/17.
 */
public class DelayQueue {

    public static void main(String[] args) throws Exception {

        String host = "192.168.1.204";
        int port = 5672;
        String username = "dengjie";
        String password = "dengjie";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("MAIN_QUEUE", true, false, false, null);
        channel.queueBind("MAIN_QUEUE", "amq.direct", "MAIN_QUEUE");

        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange", "amq.direct");
        arguments.put("x-dead-letter-routing-key", "MAIN_QUEUE");
        channel.queueDeclare("DELAY_QUEUE", true, false, false, arguments);

        // put message into delay_queue
        long delayMillis = 2 * 1000;
        String message = "hello world";
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        AMQP.BasicProperties properties = builder.expiration(String.valueOf(delayMillis)).deliveryMode(2).build();
        channel.basicPublish("", "DELAY_QUEUE", properties, SerializationUtils.serialize(message));
        // 1439792382589
        System.out.println(String.format("[*] publish to DELAY_QUEUE %s", System.currentTimeMillis()));

        channel.close();
        connection.close();

    }

}
