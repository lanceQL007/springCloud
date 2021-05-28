package com.ql.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/28 20:51
 */
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello(){
        return helloService.hello();
    }
    @GetMapping("/hello2")
    public void helloExtend(){
        HelloCommand helloCommand = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("java")), restTemplate);
       // helloCommand.run();//错误的写法
        /**
         * 两种方法只能选中一种执行
         * 直接执行,方法一
         */
        String execute = helloCommand.execute();
        System.out.println(execute);
        /**
         * 方法二，先入队，后执行
         */
        HelloCommand helloCommand2 = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("java")), restTemplate);
        try {
            Future<String> queue = helloCommand2.queue();
            String s = queue.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("hello3")
    public void hello3(){
        Future<String> future = helloService.hello2();
        try {
            String s = future.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
