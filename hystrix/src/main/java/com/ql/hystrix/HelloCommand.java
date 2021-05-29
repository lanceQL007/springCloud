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

    public HelloCommand(Setter setter, RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }

    /**
     * run方法，真正发起请求的地方
     * @return
     * @throws Exception
     */
    @Override
    protected String run() throws Exception {
        int i = 1 / 0;
        return restTemplate.getForObject("http://provider/hello",String.class);
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
