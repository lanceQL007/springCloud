package com.ql.configserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/12 12:38
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //所以请求都要登录
                .anyRequest().authenticated()
                .and()
                //运行httpBasic登录
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
