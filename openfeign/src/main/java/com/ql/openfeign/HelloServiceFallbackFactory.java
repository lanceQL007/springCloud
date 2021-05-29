package com.ql.openfeign;

import feign.hystrix.FallbackFactory;
import org.ql.commons.User;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 18:57
 */
@Component
public class HelloServiceFallbackFactory implements FallbackFactory<HelloService> {
    @Override
    public HelloService create(Throwable throwable) {
        return new HelloService() {
            @Override
            public String hello() {
                return "error---";
            }

            @Override
            public String hello2(String name) {
                return "error---";
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
        };
    }
}
