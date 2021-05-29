package com.ql.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.ql.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return helloService.hello();
    }
    @GetMapping("/hello2")
    public void helloExtend(){
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        HelloCommand helloCommand = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("java")), restTemplate,"java");
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
        HelloCommand helloCommand2 = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("java")), restTemplate,"java");
        try {
            Future<String> queue = helloCommand2.queue();
            String s = queue.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ctx.close();
    }

    @GetMapping("/hello3")
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

    @GetMapping("/hello4")
    public void hello4(){
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        //第一次请求完，数据已经缓存下来了
        String java = helloService.hello3("java");
        //删除数据，同时缓存中的数据也会被删除
        helloService.deleteUserByName("java");
        //第二次请求，虽然参数还是java，但是缓存数据已经没了，所以这一次，provider还是会受到请求
        java = helloService.hello3("java");
        ctx.close();
    }

    @GetMapping("/hello5")
    public void hello5() throws ExecutionException, InterruptedException {
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        UserCollapseCommand cmd1 = new UserCollapseCommand(userService, 99);
        UserCollapseCommand cmd2 = new UserCollapseCommand(userService, 98);
        UserCollapseCommand cmd3 = new UserCollapseCommand(userService, 97);
        Future<User> q1 = cmd1.queue();
        Future<User> q2 = cmd2.queue();
        Future<User> q3 = cmd3.queue();
        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();
        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        TimeUnit.SECONDS.sleep(2);
        UserCollapseCommand cmd4 = new UserCollapseCommand(userService, 96);
        Future<User> q4 = cmd4.queue();
        User u4 = q4.get();
        System.out.println(u4);
        ctx.close();
    }

    @GetMapping("/hello6")
    public void hello6() throws ExecutionException, InterruptedException {
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        Future<User> q1 = userService.getUserById(99);
        Future<User> q2 = userService.getUserById(98);
        Future<User> q3 = userService.getUserById(97);
        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();
        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        TimeUnit.SECONDS.sleep(2);

        Future<User> q4 = userService.getUserById(96);
        User u4 = q4.get();
        System.out.println(u4);
        ctx.close();
    }
}
