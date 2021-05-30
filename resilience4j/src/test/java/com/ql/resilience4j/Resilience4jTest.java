package com.ql.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.junit.Test;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/5/30 14:11
 */
public class Resilience4jTest {
    @Test
    public void test1() {
        //获取一个CircuitBreakerRegistry实例，可以调用ofDefaults获取一个CircuitBreakerRegistry实例，也可以自定义属性
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();

        //自定义创建实例
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                //故障率阈值百分比，超过这个阈值，断路器就会打开
                .failureRateThreshold(50)
                //断路器保持打开的时间，在到达设置的时间之后，断路器会进入half open状态
                .waitDurationInOpenState(Duration.ofMillis(1000))
                //当断路器处于half open 状态时，环形缓冲区的大小
                .ringBufferSizeInClosedState(2)
                //当断路器处于half close 状态时，环形缓冲区的大小
                .ringBufferSizeInClosedState(2)
                .build();
        CircuitBreakerRegistry r1 = CircuitBreakerRegistry.of(config);
        //两种配置方式
        CircuitBreaker cb1 = r1.circuitBreaker("java");
        CircuitBreaker cb2 = r1.circuitBreaker("java2", config);

        CheckedFunction0<String> supplier = CircuitBreaker.decorateCheckedSupplier(cb1, () -> "hello resilience4j");
        Try<String> result = Try.of(supplier)
                .map(v -> v + " hello world");
        System.out.println(result.isSuccess());
        System.out.println(result.get());
    }

    @Test
    public void test2() {
        //自定义创建实例
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                //故障率阈值百分比，超过这个阈值，断路器就会打开
                .failureRateThreshold(50)
                //断路器保持打开的时间，在到达设置的时间之后，断路器会进入half open状态
                .waitDurationInOpenState(Duration.ofMillis(1000))
                //当断路器处于half close 状态时，环形缓冲区的大小
                .ringBufferSizeInClosedState(2)
                .build();
        CircuitBreakerRegistry r1 = CircuitBreakerRegistry.of(config);
        CircuitBreaker cb1 = r1.circuitBreaker("java");
        //获取断路器的状态
        System.out.println(cb1.getState());

        cb1.onError(0, TimeUnit.MINUTES, new RuntimeException());
        System.out.println(cb1.getState());
        cb1.onError(0, TimeUnit.MINUTES, new RuntimeException());
        System.out.println(cb1.getState());
        //使用reset()后，会清除上面的异常数据，后面的方法又可以正常执行
        // cb1.reset();
        //断路器打开，后面无法执行
        CheckedFunction0<String> supplier = CircuitBreaker.decorateCheckedSupplier(cb1, () -> "hello resilience4j");
        Try<String> result = Try.of(supplier)
                .map(v -> v + " hello world");
        System.out.println(result.isSuccess());
        System.out.println(result.get());
    }

    @Test
    public void test3() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                //刷新周期
                .limitRefreshPeriod(Duration.ofMillis(1000))
                //阈值刷新频率
                .limitForPeriod(4)
                //冷却时间
                .timeoutDuration(Duration.ofMillis(1000))
                .build();

        RateLimiter rateLimiter = RateLimiter.of("java", config);
        CheckedRunnable checkedRunnable = RateLimiter.decorateCheckedRunnable(rateLimiter, () -> {
            System.out.println(new Date());
        });

        Try.run(checkedRunnable)
                .andThenTry(checkedRunnable)
                .andThenTry(checkedRunnable)
                .andThenTry(checkedRunnable)
                .onFailure(t->System.out.println(t.getMessage()));
    }

    @Test
    public void test4(){
        RetryConfig config = RetryConfig.custom()
                //重试次数（在这个次数里面的异常可以忽略）
                .maxAttempts(5)
                //重试间隔
                .waitDuration(Duration.ofMillis(500))
                //重试异常
                .retryExceptions(RuntimeException.class)
                .build();

        Retry retry = Retry.of("java", config);
        Retry.decorateRunnable(retry, new Runnable() {
            int count=0;
            //开启重试功能之后，run方法执行时，如果抛出异常，会自动触发重试异常
            @Override
            public void run() {
                if(count++<3){
                    System.out.println(count);
                    throw new RuntimeException();
                }
            }
        }).run();
    }
}
