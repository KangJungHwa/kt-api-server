package com.kt.api.config;



import com.kt.api.model.entity.MessageQueueEntity;
import com.kt.api.receiver.MQReceiver;
import com.kt.api.repository.MqmappingRepository;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Configuration
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMqConfiguration {
    private static String host;
    private static int port;
    private static String username;
    private static String password;

    //static 변수를 config file 가져오려면 set 메소드를 선언해야 한다.
    @Value("${spring.rabbitmq.host}")
    public void setHost(String host) {
        this.host = host;
    }

    @Value("${spring.rabbitmq.port}")
    public void setPort(int port) {
        this.port = port;
    }

    @Value("${spring.rabbitmq.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${spring.rabbitmq.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    //Connection과 Channel은 싱글톤을 사용한다.
    private static com.rabbitmq.client.ConnectionFactory connectionFactory = null;
    private static Connection connection = null;
    private static Channel channel = null;

    @Autowired
    MqmappingRepository mqmappingRepository;



    public List<MessageQueueEntity> sendQueueList;
    public List<MessageQueueEntity> receiveQueueList;


    //PostConstruct를 사용한 이유는 autowired 가 실행되고 난후에 실행되어야 null point 예외가 발생하지 않는다.
    @PostConstruct
    private void getSendQueueList() throws IOException, TimeoutException {
        sendQueueList = mqmappingRepository.findByDirection("send");
        for (MessageQueueEntity q : sendQueueList) {
            log.info(q.getQueueName());
        }
    }
    //PostConstruct를 사용한 이유는 autowired 가 실행되고 난후에 실행되어야 null point 예외가 발생하지 않는다.
    @PostConstruct
    private void getReceiveQueueList() throws IOException, TimeoutException {
        receiveQueueList = mqmappingRepository.findByDirection("receive");
        for (MessageQueueEntity q : receiveQueueList) {
            log.info(q.getQueueName());
        }
    }


   @PostConstruct
    public static com.rabbitmq.client.ConnectionFactory getConnectionFactory() {
                ConnectionFactory connectionFactory = new ConnectionFactory();

                connectionFactory.setHost(host);
                connectionFactory.setPort(port);
                connectionFactory.setUsername(username);
                connectionFactory.setPassword(password);
        return connectionFactory;
    }


    //bean 등록 전에 connection과 channel이 생성되어야 해서 PostConstruct 사용
    @PostConstruct
    public static Connection getConnection() {
        if (connection == null) {
            try {
                   connection = getConnectionFactory().newConnection();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    //bean 등록 전에 connection과 channel이 생성되어야 해서 PostConstruct 사용
    @PostConstruct
    public static Channel getChannel() throws IOException {
        if (channel == null) {
            channel = getConnection().createChannel();
        }
        return channel;
    }

    //rabbitMq에서 topic은 bean으로 등록되어야 함.
    @Bean
    public void declareExchange() throws IOException, TimeoutException {
        getChannel().exchangeDeclare("nlu-topic-exchange", BuiltinExchangeType.TOPIC, true);
    }

    //rabbitMq에서 Queue는 bean으로 등록되어야 함.
    @Bean
    public void declareQueues() throws IOException, TimeoutException {
        for (MessageQueueEntity queue : sendQueueList) {
            getChannel().queueDeclare(queue.getQueueName(), true, false, false, null);
        }
        for (MessageQueueEntity queue : receiveQueueList) {
            getChannel().queueDeclare(queue.getQueueName(), true, false, false, null);
        }
    }

    //rabbitMq에서 binding은 bean으로 등록되어야 함.
    @Bean
    public void declareBindings() throws IOException, TimeoutException {
        for (MessageQueueEntity queue : sendQueueList) {
            getChannel().queueBind(queue.getQueueName(), "nlu-topic-exchange", queue.getRouteKey());
        }
        for (MessageQueueEntity queue : receiveQueueList) {
            getChannel().queueBind(queue.getQueueName(), "nlu-topic-exchange", queue.getRouteKey());
        }
    }
    //receiveQueue에서 message listen용도 bean으로 등록되어야 함.
    @Bean("listener")
    public MessageListenerContainer messageListenerContainer(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        for (MessageQueueEntity queue : receiveQueueList) {
            simpleMessageListenerContainer.addQueueNames(queue.getQueueName());
        }
        /* 아래 for 문은 send 메세지 확인용
        for (MessageQueueEntity queue : sendQueueList) {
            simpleMessageListenerContainer.addQueueNames(queue.getQueueName());
        }*/
        simpleMessageListenerContainer.setMessageListener(new MQReceiver());
        return simpleMessageListenerContainer;
    }

}