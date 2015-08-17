package cn.studease.util.mom.lesson3;


import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.Date;

public class EmitLog {
    private final static String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) throws IOException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.204");
        factory.setPort(5672);
        factory.setUsername("dengjie");
        factory.setPassword("dengjie");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 开启confirm
        channel.confirmSelect();
        // 声明转发器和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = new Date().toLocaleString() + " : log something";

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("ConfirmListener handleAck : callback at %s", System.currentTimeMillis()));
                System.out.println(String.format("ConfirmListener handleAck deliveryTag: %s, multiple: %s", deliveryTag, multiple));
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("ConfirmListener handleNack deliveryTag: %s, multiple: %s", deliveryTag, multiple));
            }
        });

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(String.format("ReturnListener handleReturn replyCode: %s, replyText: %s, exchange: %s, routingKey: %s", replyCode, replyText, exchange, routingKey));
            }
        });

        channel.addFlowListener(new FlowListener() {
            @Override
            public void handleFlow(boolean active) throws IOException {
                System.out.println(String.format("FlowListener handleFlow active: %s", active));
            }
        });
        // 往转发器上发送消息
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.close();
        connection.close();


    }

}
