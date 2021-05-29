package com.ql.provider;

import org.ql.commons.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 14:16
 */
@RestController
public class UserController {
    /**
     * 假设consumer传过来的多个id的格式时1，2,3,4，···
     * @param ids
     * @return
     */
    @GetMapping("/user/{ids}")
    public List<User> getUserByIds(@PathVariable String ids){
        System.out.println(ids);
        String[] split = ids.split(",");
        List<User> users=new ArrayList<>();
        for (String s : split) {
            User u = new User();
            u.setId(Integer.parseInt(s));
            users.add(u);
        }
        System.out.println(users);
        return users;
    }
}
