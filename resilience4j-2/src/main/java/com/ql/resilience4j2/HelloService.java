package com.ql.resilience4j2;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/30 15:36
 */
@Service
//@Retry(name = "retryA")//表示yml使用的重试策略
@CircuitBreaker(name = "cbA",fallbackMethod = "error")
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    public String hello(){
        for (int i = 0; i < 5; i++) {
            restTemplate.getForObject("http://localhost:1113/hello",String.class);
        }
        return "success";
    }

    public String error(Throwable t){
        return "error";
    }
}
