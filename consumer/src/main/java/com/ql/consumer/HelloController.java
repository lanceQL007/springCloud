package com.ql.consumer;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.List;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/15 20:16
 */
@RestController
public class HelloController {
    @GetMapping("/hello1")
    public String hello1() {
        HttpURLConnection con = null;
        try {
            URL url = new URL("http://localhost:1113/hello");
            con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    //注入RestTemplate
    @Autowired
    @Qualifier("restTemplateOne")
    RestTemplate restTemplateOne;
    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/hello2")
    public String hello2() {
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        ServiceInstance instance = list.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        //restTemplate对连接的操作进行了封装
        String s = restTemplateOne.getForObject(sb.toString(), String.class);
        return s;
    }
    //有两个restTemplate时，需要注解备注一下是哪一个RestTemplate
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;
    @GetMapping("/hello3")
    public String hello3() {
        //有负载均衡可以给模糊的地址，而上面的没有负载均衡的不能给模糊的地址，两者不能混用，如过下面这个用了上面sb.toString()的拼接样式也会报错
        return restTemplate.getForObject("http://provider/hello",String.class);
    }
    //测试提交一下融合
    @GetMapping("/hello4")
    public String hello4() {
        return restTemplate.getForObject("http://provider/hello",String.class);
    }
}
