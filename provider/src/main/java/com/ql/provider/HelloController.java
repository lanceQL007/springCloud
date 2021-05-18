package com.ql.provider;

import org.ql.commons.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 以key,value的形式传递
     * @param user
     * @return
     */
    @PostMapping("/user1")
    public User addUser1(User user){
        return user;
    }

    /**
     * 以json的形式传递
     * @param user
     * @return
     */
    @PostMapping("/user2")
    public User addUser2(@RequestBody User user){
        return user;
    }

}
