package com.kt.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopicConfig {
    @Bean
    Queue sendQueue() {
        return new Queue("queue.send", false);
    }

    @Bean
    Queue receiveQueue() {
        return new Queue("queue.receive", false);
    }
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topic.nlu");
    }
    //routekey.send 로 들어오면 sendQueue로 활당
    @Bean
    Binding sendBinding(Queue sendQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(sendQueue).to(topicExchange).with("routekey.send");
    }
    //routekey.receive 로 들어오면 recieveQueue로 활당
    @Bean
    Binding receiveBinding(Queue receiveQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(receiveQueue).to(topicExchange).with("routekey.receive");
    }

}

