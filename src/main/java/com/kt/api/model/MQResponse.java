package com.kt.api.model;

import lombok.Data;

@Data
public class MQResponse {
    private StatusEnum status;
    private String message;
    private Object data;
}
