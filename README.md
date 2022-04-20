# AI Platform API Server for KT

KT AI Platform의 백엔드 연결 서비스를 제공하는 API Server

## Build

```
# mvn -Dmaven.test.skip clean package

```


mariaDB 설치 이후 timezone변경
```
SET time_zone = 'Asia/Seoul';
SHOW GLOBAL VARIABLES LIKE 'time_zone';

SET GLOBAL time_zone = 'Asia/Seoul';
SHOW SESSION VARIABLES LIKE 'time_zone';
```

command line 실행 명령
```$xslt
nohup java -jar 'kt-api-server-1.0.0-SNAPSHOT.jar' & > /dev/null
```
스프링 부트 rabbitmq 설정
발행을 할때 exchage나 queue가 없어도 에러가 발생하지 않음.
다만 구독을 하때는 없으면 에러가 남.
spring boot가 재구동 되어도 에러가 나지 않는 방법을 적용해야 함.
```$xslt
spring.rabbitmq.dynamic=true
```

###########################################
 message send(분석요청)
###########################################

1. 소베텍이 json 형태로 직접 message queue에 전송
   
    private static final String queueName = "nlu.send.queue";
    private static final String exchangeName = "nlu.send.topic";
    private static final String routingKey = "kt.nlu.send.#";

	spring:
	  rabbitmq:
		host: 10.233.28.69
		port: 5672
		username: admin
		password: Ktaicc@1234
	
2. NluSendMessageListener 에서 
   1. json을 Object 변환
   2. request history 테이블에 저장
   3. 소베텍이 정의하는 nlu 서버로 request 전송
   
###########################################
 message receive(결과저장)
###########################################
1. 소베텍이 결과의 진행율과 최종 결과를 message queue에 전송

    private static final String queueName = "nlu.receive.queue";
    private static final String exchangeName = "nlu.receive.topic";
    private static final String routingKey = "kt.nlu.receive.#";

2. NluReceiveMessageListener receive Message에서 
   1. json을 Object 변환
   2. request history 테이블에 저장
   3. 소베텍이 정의하는 nlu 서버로 request 전송
 