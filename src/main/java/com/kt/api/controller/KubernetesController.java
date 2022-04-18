package com.kt.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/backend/kubernetes")
public class KubernetesController extends RestTemplateController {

    @Value("${app.backend.k8s.url}")
    String k8sUrl;

    @Value("${app.backend.k8s.token}")
    String token;

    @Autowired
    @Qualifier("sslRestTemplate")
    RestTemplate restTemplate;

    @GetMapping
    @RequestMapping("/list/pods/{namespace}")
   // public ResponseEntity getListPods() {
    public ResponseEntity getListPods(@Valid @PathVariable("namespace") String nameSpace) {
        HttpEntity<String> entity = emptyGetRequestEntity(token);
        String url = k8sUrl + "namespaces/"+nameSpace+"/pods";
        System.out.println(url);
        return ResponseEntity.ok(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }

    @GetMapping
    @RequestMapping("/list/services")
    public ResponseEntity getListService() {
        HttpEntity<String> entity = emptyGetRequestEntity(token);
        String url = k8sUrl + "services";
        System.out.println(url);
        return ResponseEntity.ok(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }


}
