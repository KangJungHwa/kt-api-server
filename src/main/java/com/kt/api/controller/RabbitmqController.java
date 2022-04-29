package com.kt.api.controller;


import com.kt.api.config.RabbitMQConfig;
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
import java.nio.charset.Charset;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {


    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    private static final String EXCHANGE_NAME = "topic_nlu";


    @RequestMapping(value = "/send", method = RequestMethod.POST)
        public ResponseEntity<MQResponse> getKubeletLog(@RequestBody @Valid MQRequest request) throws InterruptedException{
        log.info("{}", String.format("'%s' message를 전송합니다.", request.getRequestMessage()));

        String routingKey=request.getRouteKey();
        String message=request.getRequestMessage();

        try {

            Channel channel = RabbitMQConfig.getChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }

        MQResponse response = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        response.setStatus(StatusEnum.OK);
        response.setMessage("Message Send Success!");
        response.setData("Message Send Success!");
        System.out.println("Message sent to the RabbitMQ Topic Exchange Successfully") ;

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
