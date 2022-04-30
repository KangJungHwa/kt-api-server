# AI Platform API Server for KT

KT AI Platform의 백엔드 연결 서비스를 제공하는 API Server

###  Build

```
# mvn -Dmaven.test.skip clean package

```
###  mariaDB 설치 이후 timezone변경
```
SET time_zone = 'Asia/Seoul';
SHOW GLOBAL VARIABLES LIKE 'time_zone';

SET GLOBAL time_zone = 'Asia/Seoul';
SHOW SESSION VARIABLES LIKE 'time_zone';
```

### timezone 확인
```$xslt
select @@global.time_zone, @@session.time_zone
```
### JDBC URL timezone 설정
```$xslt
jdbc:log4jdbc:mysql://10.233.4.179:3306/nlu?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul&rewriteBatchedStatements=true
```


### password 변경작업
```$xslt
UPDATE mysql.user SET Password=PASSWORD('ktaicc@1234') WHERE User='root';

GRANT USAGE ON *.* TO 'root'@'localhost' IDENTIFIED BY 'xxxxxxx';
GRANT USAGE ON *.* TO 'root'@'%' IDENTIFIED BY 'xxxxxxx';


FLUSH PRIVILEGES;
```

### command line 실행 명령
```$xslt
nohup java -jar 'kt-k8s-monitoring-1.0.0-SNAPSHOT.jar' & > /dev/null
```


###  MessageQueue RouteKey 매핑정보
```$xslt
CREATE TABLE nlu.nlu_mq_mapping (
  `id` int(11) NOT NULL,
  `direction` varchar(20) DEFAULT NULL,
  `queue_name` varchar(40) DEFAULT NULL,
  `route_key` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



insert into nlu.nlu_mq_mapping values(1,'send','IntentService','intent.service.*');
insert into nlu.nlu_mq_mapping values(2,'send','IntentTrain','intent.train.*');
insert into nlu.nlu_mq_mapping values(3,'send','NerService','ner.service.*');
insert into nlu.nlu_mq_mapping values(4,'send','NerTrain','ner.train.*');
insert into nlu.nlu_mq_mapping values(5,'receive','IntentServiceResult','intent.service.result.*');
insert into nlu.nlu_mq_mapping values(6,'receive','IntentTrainResult','intent.train.result.*');
insert into nlu.nlu_mq_mapping values(7,'receive','NerServiceResult','ner.service.result.*');
insert into nlu.nlu_mq_mapping values(8,'receive','NerTrainResult','ner.train.result.*');
```