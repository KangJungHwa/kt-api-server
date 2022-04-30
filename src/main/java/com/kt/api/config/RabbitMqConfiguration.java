package com.kt.api.config;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitMqConfiguration {
    private static String host;
    private static int port;
    private static String username;
    private static String password;

    @Value("${spring.rabbitmq.host}")
    public void setHost(String host) {
        this.host = host;
    }
    @Value("${spring.rabbitmq.port}")
    public void setPort(int port) {
        this.port=port;
    }
    @Value("${spring.rabbitmq.username}")
    public void setUsername(String username) {
        this.username=username;
    }
    @Value("${spring.rabbitmq.password}")
    public void setPassword(String password) {
        this.password=password;
    }

    private static Connection connection = null;
    private static Channel channel= null;
    @PostConstruct
    public static Connection getConnection() {
        if (connection == null) {
            try {
                ConnectionFactory connectionFactory = new ConnectionFactory();

                connectionFactory.setHost(host);
                connectionFactory.setPort(port);
                connectionFactory.setUsername(username);
                connectionFactory.setPassword(password);

                connection = connectionFactory.newConnection();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    @Bean
    public static Channel getChannel() throws IOException {
        if (connection == null) {
             channel =getConnection().createChannel();
        }
        return channel;
    }
    @Bean
    public  void declareExchange() throws IOException, TimeoutException {
        Channel channel = getConnection().createChannel();
        //Create Topic Exchange
        channel.exchangeDeclare("nlu-topic-exchange", BuiltinExchangeType.TOPIC, true);
        channel.close();
    }
    @Bean
    public  void declareQueues() throws IOException, TimeoutException {
        //Create a channel - do not share the Channel instance
        Channel channel = getConnection().createChannel();
        //Create the Queues
        channel.queueDeclare("intentTrain", true, false, false, null);
        channel.queueDeclare("intentService", true, false, false, null);
        channel.queueDeclare("nerTrain", true, false, false, null);
        channel.queueDeclare("nerService", true, false, false, null);
        channel.close();
    }

    @Bean
    public  void declareBindings() throws IOException, TimeoutException {
        Channel channel = getConnection().createChannel();
        //Create bindings - (queue, exchange, routingKey) - routingKey != null
        channel.queueBind("intentTrain", "nlu-topic-exchange", "intent.train.*");
        channel.queueBind("intentService", "nlu-topic-exchange", "intent.service.*");
        channel.queueBind("nerTrain", "nlu-topic-exchange", "ner.train.*");
        channel.queueBind("nerService", "nlu-topic-exchange", "ner.service.*");
        channel.close();
    }
}
