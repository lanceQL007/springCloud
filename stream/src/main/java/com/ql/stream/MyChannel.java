package com.ql.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/12 14:53
 */
public interface MyChannel {
    String INPUT="javaboy-input";
    String OUTPUT="javaboy-output";

    @Output(OUTPUT)
    MessageChannel output();

    @Input(INPUT)
    SubscribableChannel intput();
}
