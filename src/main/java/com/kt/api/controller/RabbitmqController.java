package com.kt.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.api.config.RabbitMqConfiguration;
import com.kt.api.model.MQRequest;
import com.kt.api.model.MQResponse;
import com.kt.api.model.StatusEnum;


import com.kt.api.model.entity.MessageQueueEntity;
import com.kt.api.repository.MqmappingRepository;
import com.kt.api.util.ApplicationContextHolder;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;

import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {



    private static ApplicationContext  context;

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
        response.setMessage("Send Message  Success!");
        response.setData("Send Message Success!");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);

    }

    @RequestMapping(value = "/addqueue", method = RequestMethod.POST)
    public ResponseEntity<MQResponse> addQueue(@RequestBody List<MessageQueueEntity> requestList) throws IOException {
        /**
         * ApplicationContext 는 한번 @Autowired로 인스턴스가 생성된 클래스를 재사용하기 위해 사용한다.
         */

        MqmappingRepository repo = context.getBean(MqmappingRepository.class);
        SimpleMessageListenerContainer listener = context.getBean("listener",SimpleMessageListenerContainer.class);

        repo.saveAll(requestList);

        for (MessageQueueEntity mq:requestList){
            RabbitMqConfiguration.getChannel().queueDeclare(mq.getQueueName(), true, false, false, null);
            RabbitMqConfiguration.getChannel().queueBind(mq.getQueueName(), "nlu-topic-exchange", mq.getRouteKey());
            //리시버 등록
            if (mq.equals("receive")) {
                listener.addQueueNames(mq.getQueueName());
            }
        }

        MQResponse response = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        response.setStatus(StatusEnum.OK);
        response.setMessage("Add Message Queue Success!");
        response.setData("Add Message Queue Success!");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
    @RequestMapping(value = "/delqueue", method = RequestMethod.POST)
    public ResponseEntity<MQResponse> delQueue(@RequestBody List<MessageQueueEntity> requestList) throws IOException {
        context  = ApplicationContextHolder.get();;
        MqmappingRepository repo = context.getBean(MqmappingRepository.class);
        for (MessageQueueEntity queue:requestList) {
            repo.delete(queue);
            RabbitMqConfiguration.getChannel().queueUnbind(queue.getQueueName(), "nlu-topic-exchange", queue.getRouteKey());
            RabbitMqConfiguration.getChannel().queueDelete(queue.getQueueName());
        }
        MQResponse response = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        response.setStatus(StatusEnum.OK);
        response.setMessage("Delete Message Queue Success!");
        response.setData("Delete Message Queue Success!");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/listqueue", method = RequestMethod.GET)
    public ResponseEntity<MQResponse> listQueue() throws IOException {
        context  = ApplicationContextHolder.get();;
        MqmappingRepository repo = context.getBean(MqmappingRepository.class);
        List<MessageQueueEntity> mqlist = repo.findAll();
        MQResponse response = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        response.setStatus(StatusEnum.OK);
        response.setMessage("Message Queue Success!");
        response.setData(mqlist);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }



}
