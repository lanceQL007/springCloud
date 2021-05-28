package com.ql.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/28 20:48
 */
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;
    /**
     * 在这个方法中，我们将发起一个远程调用，去调用provider中提供的/hello接口
     *
     * 但是，这个调用可能会失败
     *
     * 我们在这方法删添加@HystrixCommand注解，配置fallbackMethod属性，这个属性表示该方法调用失败时的灵石替代方法
     *
     * error服务降级
     * @return
     */
    @HystrixCommand(fallbackMethod = "error")
    public String hello(){
        return restTemplate.getForObject("http://provider/hello",String.class);
    }

    /**
     * 注意，这个方法名字要和fallbackMethod一致
     * 方法返回值也要和对应的方法一致
     * @return
     */
    public String error(){
        return "error";
    }
}
