package com.ql.openfeign;

import org.ql.api.IUserService;
import org.ql.commons.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 17:01
 */
//@FeignClient(value = "provider",fallback = HelloServiceFallback.class)通过注解的形式
@FeignClient(value = "provider",fallbackFactory = HelloServiceFallbackFactory.class)//通过自定义的形式是吸纳降级
public interface HelloService extends IUserService {

}
