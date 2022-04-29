package com.kt.api.config;

import com.kt.api.model.entity.MessageQueueEntity;
import com.kt.api.repository.MqmappingRepository;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *     topicExchange 방식과 directExchage 방식의 차이점은 routekey가  정확히 일치할 필요가 없다.
 *     라우팅에 사용되는 패턴은 정규식 형식일 수 있다. 즉, 점(.), 별표(*) 또는 해시(#)와 같은 기호를 사용할 수 있다.
 * 사용방법
 * 			amqpTemplate.convertAndSend(exchange, routingKey, msg);
 */
@Configuration
public class RabbitMQTopicConfig {


    @Autowired
    MqmappingRepository mqmappingRepository;

    @PostConstruct
    void init(){
        List<MessageQueueEntity> sendList= mqmappingRepository.findByDirection("send");
    }

    @Bean
    Queue sendQueue() {
        return new Queue("queue.send", false);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topic.nlu");
    }

    @Bean
    Binding sendBinding(Queue sendQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(sendQueue).to(topicExchange).with("routekey.send*");
    }

}

