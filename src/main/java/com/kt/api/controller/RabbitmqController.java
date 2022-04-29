package com.kt.api.controller;


import com.kt.api.model.MQRequest;
import com.kt.api.model.MQResponse;
import com.kt.api.model.Records;
import com.kt.api.model.StatusEnum;
import com.kt.api.producer.RabbitMQProducers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    RabbitMQProducers producer;

    private static final String topicExchange = "topic.nlu";
    private static final String sendRoutingKey="routekey.send";
    //private static final String receiveRoutingKey="routekey.receive";

    @RequestMapping(value = "/send", method = RequestMethod.POST)
        public ResponseEntity<MQResponse> getKubeletLog(@RequestBody @Valid MQRequest request) throws InterruptedException{
        log.info("{}", String.format("'%s' message를 전송합니다.", request.getRequestMessage()));


        //using 1st routing
        String content = "Tomcat Started Successfully....";
        String routingKey = "sys.dev.info";

        // send to RabbitMQ
        producer.produce(new Records(content, routingKey));

        //using 2nd routing
        content = "Exception is noticed..";
        routingKey = "sys.test.error";

        // send to RabbitMQ
        producer.produce(new Records(content, routingKey));

        //using 3rd routing
        content = "Here is your Message..";
        routingKey = "app.prod.error";

        // send to RabbitMQ
        producer.produce(new Records(content, routingKey));

        MQResponse message = new MQResponse();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
        message.setStatus(StatusEnum.OK);
        message.setMessage("Message Send Success!");
//        //현재는 테스트로 Message Send Success!라고 넣어 놨는데 실제 정보전달이 필요한경우 object 형태에 담아서 전달 한다.
        message.setData("Message Send Success!");
//
//        // topicExchange는 고정으로 사용하고
//        // sendRoutingKey는 파라메터로 전달 받는다.
//        // 여기서 repository에서 데이터를 조회해서 requstParameter를 json에 세팅하고 메세지 큐에 메세지 전달
//        // message는 json 형태로 변환하는데 request 정보와 repository 정보를 조합하여 ObjectMapper.writeAsString으로 변환
//        amqpTemplate.convertAndSend(topicExchange, request.getRouteKey(), "send second message success~~~");
//
        System.out.println("Message sent to the RabbitMQ Topic Exchange Successfully") ;

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
}
