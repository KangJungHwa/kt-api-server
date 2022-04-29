package com.kt.api.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private static String rabbitmqHost;
    public static Channel getChannel(){
        Channel channel=null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitmqHost);
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }
}
