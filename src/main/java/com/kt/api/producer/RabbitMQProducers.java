package com.kt.api.producer;

import com.kt.api.model.Records;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducers {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("")
    private String exchange;

    public void produce(Records records){
        String routingKey = records.getRoutingKey();
        amqpTemplate.convertAndSend(exchange, routingKey, records);
        System.out.println("Send msg = " + records);
    }
}
