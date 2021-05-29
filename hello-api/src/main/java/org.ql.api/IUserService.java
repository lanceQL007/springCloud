package org.ql.api;

import org.ql.commons.User;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 17:46
 */

public interface IUserService {
    /**
     * 这里的方法名无所谓，随意取，符合规范
     * @return
     */
    @GetMapping("/hello")
    String hello();

    /**
     * 以key,value的形式传
     * @param name
     * @return
     */
    @GetMapping("/hello2")
    String hello2(@RequestParam("name") String name);

    /**
     * 以json的信息传参
     * @param user
     * @return
     */
    @PostMapping("/user2")
    public User addUser2(@RequestBody User user);

    /**
     * 通过url的方式传参
     * @param id
     */
    @DeleteMapping("/user2/{id}")
    void deleteUser2(@PathVariable("id") Integer id);

    /**
     * 以header的形式传参
     * @param name
     */
    @GetMapping("/user3")
    void getUserByName(@RequestHeader("name") String name) throws UnsupportedEncodingException;
}
