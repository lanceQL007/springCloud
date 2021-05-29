package com.ql.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

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
    @HystrixCommand(fallbackMethod = "error",ignoreExceptions = ArithmeticException.class)
    public String hello(){
        int i = 1 / 0;
        return restTemplate.getForObject("http://provider/hello",String.class);
    }

    @HystrixCommand(fallbackMethod = "error2")
    @CacheResult//这个注解表示该方法的请求结果会被缓存起来，默认情况下，缓存的key就是方法的参数，缓存的value就是返回值
    public String hello3(String name){
        return restTemplate.getForObject("http://provider/hello2?name={1}",String.class,name);
    }

    @HystrixCommand
    @CacheRemove(commandKey = "hello3")
    public String deleteUserByName(String name){
        return null;
    }

    public String error2(String name){
        return "error2 java";
    }

    @HystrixCommand(fallbackMethod = "error")
    public Future<String> hello2(){
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return restTemplate.getForObject("http://provider/hello",String.class);
            }
        };
    }

    /**
     * 注意，这个方法名字要和fallbackMethod一致
     * 方法返回值也要和对应的方法一致
     * @return
     */
    public String error(Throwable t){
        return "error"+t.getMessage();
    }
}
