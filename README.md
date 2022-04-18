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
