package com.kt.api.consumer;

import com.kt.api.model.Records;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumers {
    @RabbitListener(queues="${spring.rabbitmq.queue}", containerFactory="jsaFactory")
    public void recievedMessage(Records records) {
        System.out.println("Recieved Message: " + records);
    }
}
