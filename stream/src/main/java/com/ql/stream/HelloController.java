package com.ql.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/12 14:56
 */
@RestController
public class HelloController {
    public final static Logger logger= LoggerFactory.getLogger(HelloController.class);

    @Autowired
    MyChannel myChannel;

    @GetMapping("/hello")
    public void hello(){
        logger.info("send msg:"+new Date());
        myChannel.output().send(MessageBuilder.withPayload("hello spring cloud stream!")
                .setHeader("x-delay",3000)
                .build());
    }
}
