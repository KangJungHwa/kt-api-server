package com.kt.api.config;

import com.kt.api.util.RabbitmqConnectionManager;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public  void declareExchange() throws IOException, TimeoutException {
        Channel channel = RabbitmqConnectionManager.getConnection().createChannel();
        //Create Topic Exchange
        channel.exchangeDeclare("nlu-topic-exchange", BuiltinExchangeType.TOPIC, true);
        channel.close();
    }
    @Bean
    public  void declareQueues() throws IOException, TimeoutException {
        //Create a channel - do not share the Channel instance
        Channel channel = RabbitmqConnectionManager.getConnection().createChannel();
        //Create the Queues
        channel.queueDeclare("intentTrain", true, false, false, null);
        channel.queueDeclare("intentService", true, false, false, null);
        channel.queueDeclare("nerTrain", true, false, false, null);
        channel.queueDeclare("nerService", true, false, false, null);
        channel.close();
    }

    @Bean
    public  void declareBindings() throws IOException, TimeoutException {
        Channel channel = RabbitmqConnectionManager.getConnection().createChannel();
        //Create bindings - (queue, exchange, routingKey) - routingKey != null
        channel.queueBind("intentTrain", "nlu-topic-exchange", "intent.train.*");
        channel.queueBind("intentService", "nlu-topic-exchange", "intent.service.*");
        channel.queueBind("nerTrain", "nlu-topic-exchange", "ner.train.*");
        channel.queueBind("nerService", "nlu-topic-exchange", "ner.service.*");
        channel.close();
    }
}
