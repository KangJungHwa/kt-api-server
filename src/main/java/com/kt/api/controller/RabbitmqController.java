package com.kt.api.controller;


import com.kt.api.model.MQRequest;
import com.kt.api.model.MQResponse;
import com.kt.api.model.StatusEnum;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.queue}")
    private String QUEUE_NAME;
    //private static final String EXCHANGE_NAME = "topic_nlu";


    @RequestMapping(value = "/send", method = RequestMethod.POST)
        public ResponseEntity<MQResponse> sendMessage(@RequestBody @Valid MQRequest request) throws IOException, TimeoutException {
        log.info("{}", String.format("'%s' message를 전송합니다.", request.getRequestMessage()));
        log.info("host : "+host);
        log.info("port : "+port);
        log.info("username : "+username);
        log.info("password : "+password);
        String routingKey=request.getRouteKey();
        String message=request.getRequestMessage();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
             channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }


        MQResponse response = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        response.setStatus(StatusEnum.OK);
        response.setMessage("Message Send Success!");
        response.setData("Message Send Success!");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
//
//
//        try {
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost(host);
//            factory.setPort(port);
//            factory.setUsername(username);
//            factory.setPassword(password);
//
//
//
//            Connection connection = factory.newConnection();
//            Channel channel = connection.createChannel();
//            //channel.exchangeDeclare(EXCHANGE_NAME, "topic",true);
//            //TODO 라우트 키에 해당하는 메세지 큐를 바인딩 해준다.
//            channel.queueBind("send.test",EXCHANGE_NAME,routingKey);
//
//            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
//            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
//
//
//
//        response.setStatus(StatusEnum.OK);
//        response.setMessage("Message Send Success!");
//        response.setData("Message Send Success!");
//        System.out.println("Message sent to the RabbitMQ Topic Exchange Successfully") ;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
    }
}
