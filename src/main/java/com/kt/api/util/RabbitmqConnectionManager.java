package com.kt.api.util;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class RabbitmqConnectionManager {


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
}
