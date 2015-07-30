package cn.studease.util.mom;

import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

/**
 * Author: liushaoping
 * Date: 2015/7/29.
 */
public class Mq {

    public static void main(final String... args) throws Exception {

        CachingConnectionFactory cf = new CachingConnectionFactory("192.168.1.204", 5672);
        cf.setUsername("dengjie");
        cf.setPassword("dengjie");

        // set up the queue, exchange, binding on the broker
        RabbitAdmin admin = new RabbitAdmin(cf);
        Queue queue = new Queue("myQueue");
        admin.declareQueue(queue);
        TopicExchange exchange = new TopicExchange("myExchange");
        admin.declareExchange(exchange);
        admin.declareBinding(
                BindingBuilder.bind(queue).to(exchange).with("foo.*"));

        // set up the listener and container
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(cf);
        Object listener = new Object() {
            public void handleMessage(String foo) {
                System.out.println("listener " + foo);
            }
        };
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
        container.setMessageListener(adapter);
        container.setQueueNames("myQueue");
        container.start();

        // set up the listener and container
        SimpleMessageListenerContainer container2 =
                new SimpleMessageListenerContainer(cf);
        Object listener2 = new Object() {
            public void handleMessage(String foo) {
                System.out.println("listener2 " + foo);
            }
        };
        MessageListenerAdapter adapter2 = new MessageListenerAdapter(listener2);
        container2.setMessageListener(adapter2);
        container2.setQueueNames("myQueue");
        container2.start();

        // send something
        RabbitTemplate template = new RabbitTemplate(cf);
        template.convertAndSend("myExchange", "foo.bar", "Hello, world!");
        template.convertAndSend("myExchange", "foo.bar", "Hello, world!2");
        template.convertAndSend("myExchange", "foo.bar", "Hello, world!3");
        template.convertAndSend("myExchange", "foo.bar", "Hello, world!4");
        Thread.sleep(1000);
        container.stop();
        container2.stop();
    }

    /**
     * 从数据库或其他什么地方load队列配置信息
     *
     * @return
     */
    private List<Object> loadQueueSetting() {
        return new ArrayList<>();
    }

    private List<Queue> initQueueFromSetting(List<Object> settings) {
        List<Queue> queues = new ArrayList<>();
        for (Object setting : settings) {
            queues.add(new Queue(setting.toString()));
        }
        return queues;
    }

    private List<Object> loadExchangeSetting() {
        return new ArrayList<>();
    }

    private List<TopicExchange> initTopicExchangeFromSetting(List<Object> settings) {
        List<TopicExchange> exchanges = new ArrayList<>();
        for (Object setting : settings) {
            exchanges.add(new TopicExchange(setting.toString()));
        }
        return exchanges;
    }

    private List<Object> loadListenerSetting() {
        return new ArrayList<>();
    }


}
