package com.kt.api.util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitmqClient {

    @Value("${app.backend.rabbitmq.url}")
    String rabbitmqUrl;

    @Value("${app.backend.rabbitmq.username}")
    String username;

    @Value("${app.backend.rabbitmq.password}")
    String password;

    @Value("${app.backend.rabbitmq.port}")
    int port;

    private Connection connection = null;
    private Channel channel = null;

    public Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqUrl);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        return this.channel;
    }

    public void close() throws IOException, TimeoutException  {
        this.channel.close();
        this.connection.close();
    }
}
