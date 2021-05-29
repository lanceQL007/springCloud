package com.ql.provider;

import org.ql.api.IUserService;
import org.ql.commons.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/15 20:10
 */
@RestController
public class HelloController implements IUserService {
    @Value("${server.port}")
    Integer port;
    @Override
    public String hello() {
        return "hello java!"+port;
    }

    @Override
    public String hello2(String name){
        System.out.println(new Date()+">>>"+name);
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
    @Override
    public User addUser2(@RequestBody User user){
        return user;
    }

    /**
     * 以key，value的形式传参
     * @param user
     */
    @PutMapping("/user1")
    public void updateUser1(User user){
        System.out.println(user);
    }

    /**
     * 以json的形式传参
     * @param user
     */
    @PutMapping("/user2")
    public void updateUser2(@RequestBody User user){
        System.out.println(user);
    }

    @DeleteMapping("/user1")
    public void deleteUser1(Integer id){
        System.out.println(id);
    }
    @Override
    public void deleteUser2(@PathVariable Integer id){
        System.out.println(id);
    }

    @Override
    public void getUserByName(@RequestHeader String name) throws UnsupportedEncodingException {
        System.out.println(URLDecoder.decode(name,"UTF-8"));
    }
}
