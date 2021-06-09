package com.ql.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    RouteLocator routeLocator(RouteLocatorBuilder builder){
//        return builder.routes()
//                //如果地址是java_route/get的话，会重新路由到http://httpbin.org
//                .route("java_route",r->r.path("/get").uri("http://www.baidu.com"))
//                .build();
//    }
}
