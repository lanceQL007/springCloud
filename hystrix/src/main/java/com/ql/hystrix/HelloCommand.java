package com.ql.hystrix;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/28 21:43
 * HelloCommand后台帮我们发送请求
 */
public class HelloCommand extends HystrixCommand<String> {

    RestTemplate restTemplate;
    String name;

    public HelloCommand(Setter setter, RestTemplate restTemplate,String name) {
        super(setter);
        this.name=name;
        this.restTemplate = restTemplate;
    }

    /**
     * run方法，真正发起请求的地方
     * @return
     * @throws Exception
     */
    @Override
    protected String run() throws Exception {
        //int i = 1 / 0;
        return restTemplate.getForObject("http://provider/hello2?name={1}",String.class,name);
    }

    @Override
    protected String getCacheKey() {
        return name;
    }

    /**
     * 这个方法是请求失败的回调
     * @return
     */
    @Override
    protected String getFallback() {
      //  return super.getFallback();
        return "error-extends"+getExecutionException().getMessage();
    }
}
