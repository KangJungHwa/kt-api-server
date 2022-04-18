package com.kt.api.controller;

import com.kt.api.controller.RestTemplateController;
import com.kt.api.repository.NodeRepository;
import com.kt.api.service.NodeService;
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
@RequestMapping("/backend/harbor")
public class HarborController extends RestTemplateController {

    @Value("${app.backend.harbor.url}")
    String harborUrl;

    @Value("${app.backend.harbor.username}")
    String username;

    @Value("${app.backend.harbor.password}")
    String password;

    @Autowired
    @Qualifier("sslRestTemplate")
    RestTemplate restTemplate;

    /////////////////////////////////////
    // User API
    /////////////////////////////////////



    @GetMapping
    @RequestMapping("/users/searchByUsername?userame={username}")
    public ResponseEntity searchUser(@PathVariable("username") String userName) {
        HttpEntity<String> entity = emptyGetRequestEntity(username, password);
        String url = harborUrl + "/users/search?username=" + userName;
        return ResponseEntity.ok(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }


    @GetMapping
    @RequestMapping("/users/{userid}")
    public ResponseEntity getUser(@Valid @PathVariable("userid") String userID) {
        HttpEntity<String> entity = emptyGetRequestEntity(username, password);
        String url = harborUrl + "/users/" + userID;
        return ResponseEntity.ok(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }

    /////////////////////////////////////
    // Metrics API
    /////////////////////////////////////

    @GetMapping
    @RequestMapping("/metrics")
    public ResponseEntity scanAllMetrics() {
        HttpEntity<String> entity = emptyGetRequestEntity(username, password);
        String url = harborUrl + "/scans/all/metrics";
        return ResponseEntity.ok(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }

    /////////////////////////////////////
    // Project API
    /////////////////////////////////////

    @GetMapping
    @RequestMapping("/projects")
    public ResponseEntity getProjetcs(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(value = "with_detail", defaultValue = "true") boolean withDetail
    ) {
        HttpEntity<String> entity = emptyGetRequestEntity(username, password);
        String url = harborUrl + String.format("/projects?page=%s&page_size=%s&with_detail=%s",
                page, pageSize, withDetail);
        return ResponseEntity.ok(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }
}
