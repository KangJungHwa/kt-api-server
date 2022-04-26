package com.kt.api.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class NluMessage {
    private String content;
    private String routingKey;


}
