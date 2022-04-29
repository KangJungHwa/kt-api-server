package com.kt.api.consumer;

import com.kt.api.util.RabbitmqConnectionManager;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class Consumers {
    public  void subscribeMessage() throws IOException, TimeoutException {
        Channel channel = RabbitmqConnectionManager.getConnection().createChannel();
        channel.basicConsume("intentService", true, ((consumerTag, message) -> {
            System.out.println("\n\n=========== intentService Queue ==========");
            System.out.println(consumerTag);
            System.out.println("intentService: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
        channel.basicConsume("intentTrain", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ intentTrain Queue ==========");
            System.out.println(consumerTag);
            System.out.println("intentTrain: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
        channel.basicConsume("nerService", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ nerService Queue ==========");
            System.out.println(consumerTag);
            System.out.println("nerService: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
        channel.basicConsume("nerTrain", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ nerTrain Queue ==========");
            System.out.println(consumerTag);
            System.out.println("nerTrain: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }
}
