package com.kt.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Timestamp;

@Data
@Entity(name = "nlu_api_request_history")
public class TrainRequestEntity {
    @Id
    @Column(name = "request_id", columnDefinition = "VARCHAR(100)", nullable = true)
    private String requestId;

    @Column(name = "project_id", columnDefinition = "VARCHAR(100)", nullable = true)
    private String projectId;

    /**
     * 호출 유형
     * intent, intent_service, ner, ner_service 등
     */
    @Column(name = "request_type", columnDefinition = "VARCHAR(100)", nullable = true)
    private String requestType;

    /**
     * 호출 URL
     * 학습 서버 주소
     */
    @Column(name = "request_url", columnDefinition = "VARCHAR(150)", nullable = true)
    private String requestUrl;

    @Column(name = "action_nm", columnDefinition = "VARCHAR(100)", nullable = true)
    private String actionNm;

    @Column(name = "create_dt", insertable = true, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDt;

    @Column(name = "update_dt", insertable = false, updatable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Timestamp updateDt;

    @Column(name = "elapsed_time_millis")
    private Long elapsedTimeMillis;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "status", columnDefinition = "VARCHAR(50)", nullable = true)
    private String status;

    @Column(name = "ip_address", columnDefinition = "VARCHAR(50)", nullable = true)
    private String ipAddress;

    @Column(name = "message", columnDefinition = "VARCHAR(150)", nullable = true)
    private String message;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String requestBody;

    @Column(name = "user_id", columnDefinition = "VARCHAR(50)", nullable = true)
    private String userId;

    @Column(name = "route_key", columnDefinition = "VARCHAR(50)", nullable = true)
    private String routeKey;

}
