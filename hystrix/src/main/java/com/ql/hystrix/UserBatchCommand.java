package com.ql.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.ql.commons.User;

import java.util.List;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 14:30
 * 解决批处理
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {
    private List<Integer> ids;
    private UserService userService;

    public UserBatchCommand(List<Integer> ids, UserService userService) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("batchCmd"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("batchKey")));
        this.ids = ids;
        this.userService = userService;
    }

    @Override
    protected List<User> run() throws Exception {
        return userService.getUsersByIds(ids);
    }
}
