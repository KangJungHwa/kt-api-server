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


###  MessageQueue RouteKey 매핑정보 테이블 및 sequence 생성
```$xslt
DROP TABLE IF EXISTS nlu.nlu_mq_mapping;
CREATE TABLE `nlu_mq_mapping` (
  `id` int(11) NOT NULL,
  `direction` varchar(20) NOT NULL,
  `queue_name` varchar(40) NOT NULL,
  `route_key` varchar(40) NOT NULL,
  `exchange` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS nlu.nlu_mq_mapping_seq;
CREATE TABLE  nlu.nlu_mq_mapping_seq (
	`id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY
);

DELIMITER //

DROP TRIGGER IF EXISTS nlu.nlu_mq_mapping_seq_insert_seq //

CREATE TRIGGER nlu.`nlu_mq_mapping_seq_insert_seq`
BEFORE INSERT ON nlu.`nlu_mq_mapping`
FOR EACH ROW
BEGIN
  INSERT INTO nlu.`nlu_mq_mapping_seq` VALUES (NULL);
  SET NEW.id = LAST_INSERT_ID();
END 

//

DELIMITER ;

트리거에 의해 seq를 0으로 입력해도 max(id) +1로 입력된다.

insert into nlu.nlu_mq_mapping values(0,'send','IntentService999','intent.service.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'send','IntentTrain','intent.train.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'send','NerService','ner.service.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'send','NerTrain','ner.train.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'receive','IntentServiceResult','intent.service.result.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'receive','IntentTrainResult','intent.train.result.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'receive','NerServiceResult','ner.service.result.*','nlu-topic-exchange');
insert into nlu.nlu_mq_mapping values(0,'receive','NerTrainResult','ner.train.result.*','nlu-topic-exchange');
```

