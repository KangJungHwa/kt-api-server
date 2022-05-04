package com.kt.api.model;

import lombok.Data;

@Data
public class DefaultResponse {
    private StatusEnum status;
    private String message;
    private Object data;
}
