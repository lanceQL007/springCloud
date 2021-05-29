package com.ql.openfeign;

import org.ql.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 17:06
 */
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello() throws UnsupportedEncodingException {
        String s = helloService.hello2("秋分扫落叶");
        System.out.println(s);

        User user = new User();
        user.setId(1);
        user.setName("java");
        user.setPassword("123");
        User u = helloService.addUser2(user);
        System.out.println(u);

        helloService.deleteUser2(2);

        helloService.getUserByName(URLEncoder.encode("秋分扫落叶","UTF-8"));

        return helloService.hello();
    }
}
