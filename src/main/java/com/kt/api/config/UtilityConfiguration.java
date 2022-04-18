package com.kt.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.api.util.SSLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Jackson 등의 유티릴티성 클래스를 Autowire하고자 하는 경우에 Bean을 정의하도록 함.
 */
@Configuration
@Slf4j
public class UtilityConfiguration {

    @Bean("mapper")
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean("restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("sslRestTemplate")
    public RestTemplate sslRestTemplate() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        return SSLUtils.getRestTemplate(null, -1);
    }

}