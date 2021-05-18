package com.ql.provider;

import org.ql.commons.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/18 23:22
 */
@Controller
public class RegisterController {
    @PostMapping("/register")
    public String register(User user){
        return "redirect:http://provider/loginPage?name="+user.getName();
    }
    @GetMapping("/loginPage")
    @ResponseBody
    public String loginPage(String name){
        return "loginPage:"+name;
    }
}
