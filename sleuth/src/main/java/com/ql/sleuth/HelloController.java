package com.ql.sleuth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/12 18:49
 */
@RestController
public class HelloController {
    private static final Log log= LogFactory.getLog(HelloController.class);

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello(){
        log.info("hello Spring Cloud sleuth!");
        return "hello Spring Cloud sleuth!";
    }
    @GetMapping("/hello2")
    public String hello2() throws InterruptedException {
        log.info("hello2 sleuth!");
        TimeUnit.SECONDS.sleep(5);
        return restTemplate.getForObject("http://localhost:8080/hello3",String.class);
    }
    @GetMapping("/hello3")
    public String hello3() throws InterruptedException {
        log.info("hello3!");
        TimeUnit.SECONDS.sleep(5);
        return "hello3!";
    }
    @GetMapping("/hello4")
    public String hello4() throws InterruptedException {
        log.info("hello4!");

        return helloService.backgroundFun();
    }
}