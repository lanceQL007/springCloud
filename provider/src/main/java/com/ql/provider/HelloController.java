package com.ql.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/15 20:10
 */
@RestController
public class HelloController {
    @Value("${server.port}")
    Integer port;
    @GetMapping("/hello")
    public String hello() {
        return "hello java!"+port;
    }
    @GetMapping("/hello2")
    public String hello2(String name){
        return "hello "+name;
    }
}
