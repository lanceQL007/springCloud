package com.ql.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/11 17:38
 */
@RestController
@RefreshScope
public class HelloController {
    @Value("${java1}")
    String java1;
    @GetMapping("/hello")
    public String hello() {
        return java1;
    }
}
