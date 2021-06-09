package com.ql.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/9 13:14
 */
@Component
public class PermissFilter extends ZuulFilter {
    /**
     * 过滤器的类型，权限判断一般是pre
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器的优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 核心的过滤逻辑
     * @return  这个方法虽然有返回值，但是现在这个返回值目前无所谓
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
         //获取当前请求
        HttpServletRequest request = ctx.getRequest();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(!Objects.equals("java",username)||!Objects.equals("123",password)){
            //如果请求条件不满足的话，直接从这里给出响应
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.addZuulResponseHeader("content-type","text/html;charset=utf-8");
            ctx.setResponseBody("非法请求");
        }
        return null;
    }
}
