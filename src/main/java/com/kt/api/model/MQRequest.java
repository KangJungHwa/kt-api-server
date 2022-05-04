package com.kt.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MQRequest {


    @ApiModelProperty(name = "requestMessage", value = "requestMessage")
    @JsonProperty("requestMessage")
    private String requestMessage;


    @ApiModelProperty(name = "routeKey", value = "routeKey")
    @JsonProperty("routeKey")
    private String routeKey;


    @ApiModelProperty(name = "messageQueue", value = "messageQueue")
    @JsonProperty("messageQueue")
    private String messageQueue;
}
