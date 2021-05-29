package com.ql.openfeign;

import org.ql.commons.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 18:48
 * 使用@RequestMapping("/java")重新定义路径，否则的话，会报错（防止请求地址重复）
 */
@Component
@RequestMapping("/java")
public class HelloServiceFallback implements HelloService{
    @Override
    public String hello() {
        return "error";
    }

    @Override
    public String hello2(String name) {
        return "error";
    }

    @Override
    public User addUser2(User user) {
        return null;
    }

    @Override
    public void deleteUser2(Integer id) {

    }

    @Override
    public void getUserByName(String name) throws UnsupportedEncodingException {

    }
}
