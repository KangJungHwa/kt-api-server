package com.kt.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.kt.api.model.entity.MessageQueueEntity;
import com.kt.api.repository.MqmappingRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;


//@Configuration
@Slf4j
public class RabbitConfiguration {
//    private static String host;
//    private static int port;
//    private static String username;
//    private static String password;
//
//    //static 변수를 config file 가져오려면 set 메소드를 선언해야 한다.
//    @Value("${spring.rabbitmq.host}")
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    @Value("${spring.rabbitmq.port}")
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    @Value("${spring.rabbitmq.username}")
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    @Value("${spring.rabbitmq.password}")
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Autowired
//    private RabbitAdmin admin;
//
//     @Autowired
//    ConnectionFactory connectionFactory;
//
//    @Autowired
//    MqmappingRepository mqmappingRepository;
//
//    public List<MessageQueueEntity> sendQueueList;
//    public List<MessageQueueEntity> receiveQueueList;
//        //PostConstruct를 사용한 이유는 autowired 가 실행되고 난후에 실행되어야 null point 예외가 발생하지 않는다.
//    @PostConstruct
//    private void getSendQueueList() throws IOException, TimeoutException {
//        sendQueueList = mqmappingRepository.findByDirection("send");
//        for (MessageQueueEntity q : sendQueueList) {
//            log.info(q.getQueueName());
//        }
//    }
//    //PostConstruct를 사용한 이유는 autowired 가 실행되고 난후에 실행되어야 null point 예외가 발생하지 않는다.
//    @PostConstruct
//    private void getReceiveQueueList() throws IOException, TimeoutException {
//        receiveQueueList = mqmappingRepository.findByDirection("receive");
//        for (MessageQueueEntity q : receiveQueueList) {
//            log.info(q.getQueueName());
//        }
//    }
//
//    @Bean
//    void setQueue() {
//        for (MessageQueueEntity q : sendQueueList) {
//            Queue queue = new Queue(q.getQueueName(), true, false, false);
//            Binding binding = new Binding("nlu-topic-exchange", Binding.DestinationType.EXCHANGE, "nlu-topic-exchange", q.getRouteKey(), null);
//            admin.declareQueue(queue);
//            admin.declareBinding(binding);
//
//        }
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("nlu-topic-exchange");
//    }
//
//    @Bean
//    public RabbitAdmin rabbitAdmin() {
//        return new RabbitAdmin(connectionFactory);
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory consumerSimpleContainerFactory() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPrefetchCount(5);
//        return factory;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory consumerBatchContainerFactory() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(this.connectionFactory);
//        factory.setBatchListener(true); // configures a BatchMessageListenerAdapter
//        factory.setBatchSize(5);
//        factory.setConsumerBatchEnabled(true);
//        factory.setPrefetchCount(1);
//        return factory;
//    }
}
