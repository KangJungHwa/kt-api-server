package com.kt.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MQRequest {


    @ApiModelProperty(name = "requestMessage", value = "message")
    @JsonProperty("requestMessage")
    private String requestMessage;


    @ApiModelProperty(name = "routeKey", value = "message")
    @JsonProperty("routeKey")
    private String routeKey;
}
