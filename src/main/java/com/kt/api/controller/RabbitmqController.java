package com.kt.api.controller;


import com.kt.api.config.RabbitMqConfiguration;
import com.kt.api.model.MQRequest;
import com.kt.api.model.MQResponse;
import com.kt.api.model.StatusEnum;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {

    @RequestMapping(value = "/send", method = RequestMethod.POST)
        public ResponseEntity<MQResponse> sendMessage(@RequestBody @Valid MQRequest request) throws IOException, TimeoutException {
        log.info("{}", String.format("'%s' message를 전송합니다.", request.getRequestMessage()));
        log.info("{}", String.format("'%s' routeKey.", request.getRouteKey()));

        Channel channel = RabbitMqConfiguration.getChannel();
        String message = "Drink a lot of Water and stay Healthy!";
        channel.basicPublish("nlu-topic-exchange", request.getRouteKey(), null, message.getBytes());

        MQResponse response = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        response.setStatus(StatusEnum.OK);
        response.setMessage("Message Send Success!");
        response.setData("Message Send Success!");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);

    }

}
