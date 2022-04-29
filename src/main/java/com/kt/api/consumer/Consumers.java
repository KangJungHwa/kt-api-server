package com.kt.api.consumer;


import com.kt.api.config.RabbitMqConfiguration;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class Consumers {
    final String queueName="intentTrain";
    @RabbitListener(queues = queueName)
    public void recievedMessage(String msg) {
        log.info("Recieved Message From RabbitMQ: " + msg);
    }
}
