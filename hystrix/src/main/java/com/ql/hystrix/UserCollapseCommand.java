package com.ql.hystrix;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import org.ql.commons.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/29 14:36
 */
public class UserCollapseCommand extends HystrixCollapser<List<User>, User,
        Integer> {
    private UserService userService;
    private Integer id;

    public UserCollapseCommand(UserService userService, Integer id) {

        super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("UserCollapseCommand"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(200)));
        this.userService = userService;
        this.id = id;
    }

    /**
     * 请求参数
     *
     * @return
     */
    @Override
    public Integer getRequestArgument() {
        return id;
    }

    /**
     * 请求合并的方法,合并好的请求统一放在collection,等待发送，不同其中有时间限制，不可能无限制等待
     *
     * @param collection
     * @return
     */
    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Integer>> collection) {
        List<Integer> ids = new ArrayList<>(collection.size());
        for (CollapsedRequest<User, Integer> userIntegerCollapsedRequest : collection) {
            ids.add(userIntegerCollapsedRequest.getArgument());
        }
        return new UserBatchCommand(ids, userService);
    }

    /**
     * 请求结果分发
     *
     * @param users      返回的结果
     * @param collection 拿到的请求
     */

    @Override
    protected void mapResponseToRequests(List<User> users,
                                         Collection<CollapsedRequest<User, Integer>> collection) {
        int count = 0;
        for (CollapsedRequest<User, Integer> request : collection) {
            request.setResponse(users.get(count++));
        }
    }
}
