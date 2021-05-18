package com.ql.consumer;

import org.ql.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    /**
     * 注入RestTemplate
     */
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
    /**
     * 有两个restTemplate时，需要注解备注一下是哪一个RestTemplate
     */

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;
    @GetMapping("/hello3")
    public String hello3() {
        //有负载均衡可以给模糊的地址，而上面的没有负载均衡的不能给模糊的地址，两者不能混用，如过下面这个用了上面sb.toString()的拼接样式也会报错
        return restTemplate.getForObject("http://provider/hello",String.class);
    }
    /**
     * Get请求
     */
    @GetMapping("/hello4")
    public void hello4() {
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}", String.class, "java");
        System.out.println(s1);
        //除了包含返回结果，还包含返回信息
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider/hello2?name={1}", String.class, "java");
        System.out.println("返回的具体数据："+responseEntity.getBody());
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("返回的状态码（是一个对象）："+ statusCode);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        System.out.println("返回的状态码（int类型）："+ statusCodeValue);
        System.out.println("响应头");
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> set = headers.keySet();
        for (String s : set) {
            System.out.println(s+"---"+headers.get(s));
        }
    }
    @GetMapping("/hello5")
    public void hello5() throws UnsupportedEncodingException {
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}", String.class, "java");
        System.out.println(s1);

        HashMap<String, String> map = new HashMap<>(2);
        map.put("name","zhangsan");
        String s2 = restTemplate.getForObject("http://provider/hello2?name={name}", String.class, map);
        System.out.println(s2);
        //中文需要转码，否则会报错
        String url="http://provider/hello2?name="+ URLEncoder.encode("张三","UTF-8");
        URI uri = URI.create(url);
        String s3 = restTemplate.getForObject(uri, String.class);
        System.out.println(s3);
    }


    @GetMapping("/hello6")
    public void hello6(){
        //以key，value形式传递
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("name","java");
        map.add("password","123456");
        map.add("id",88);
        User user = restTemplate.postForObject("http://provider/user1", map, User.class);
        System.out.println(user);

        //以json的形式传递
        //断言结果为true，则继续往下执行，为false则终止，慎用
        assert user != null;
        user.setId(98);
        User user1 = restTemplate.postForObject("http://provider/user2", user, User.class);
        System.out.println(user1);
    }

    @GetMapping("/hello7")
    public void hello7(){
        //以key，value形式传递
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("name","java");
        map.add("password","123456");
        map.add("id",88);
        //返回的是重定向的地址,后面不用返回值
        URI uri = restTemplate.postForLocation("http://provider/register", map);
        System.out.println(uri);
        //通过重定向的地址进行跳转
        String s = restTemplate.getForObject(uri, String.class);
        System.out.println(s);

    }
}
