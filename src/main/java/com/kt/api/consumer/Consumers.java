package com.kt.api.consumer;


import com.kt.api.config.RabbitMqConfiguration;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class Consumers {
    final String queueName="intentTrain";
    @RabbitListener(queues = queueName)
    public void recievedMessage(String msg) {
        System.out.println("Recieved Message From RabbitMQ: " + msg);
    }
}
