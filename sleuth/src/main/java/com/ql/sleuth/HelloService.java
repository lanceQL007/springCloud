package com.ql.sleuth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/12 19:24
 */
@Service
public class HelloService {

    private static final Log log= LogFactory.getLog(HelloController.class);

    /**
     * 开启异步
     * @return
     */
    @Async
    public String backgroundFun(){
        log.info("Async backgroundFun");
        return "Async backgroundFun";
    }
    @Scheduled(cron = "0/10 * * * * ?")
    public void sche1(){
        log.info("start:"+new Date());
        backgroundFun();
        log.info("end:"+new Date());
    }
}
