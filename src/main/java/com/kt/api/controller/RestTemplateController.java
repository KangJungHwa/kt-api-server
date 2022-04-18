package com.kt.api.controller;

import com.kt.api.util.SSLUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

public class RestTemplateController {

    public static HttpEntity<String> emptyGetRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>("", headers);
    }

    public static HttpEntity<String> emptyGetRequestEntity(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(SSLUtils.getBasicAuth(username, password));
        return new HttpEntity<String>("", headers);
    }

    public static HttpEntity<String> emptyGetRequestEntity(String bearertoken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearertoken);
        return new HttpEntity<String>("", headers);
    }
    public static HttpEntity<String> jsonRequestEntity(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(json, headers);
    }
}
