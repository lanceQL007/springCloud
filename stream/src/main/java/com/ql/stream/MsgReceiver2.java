package com.ql.stream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.util.Date;

/**
 * @author LanceQ
 * @version 1.0
 * @time 2021/6/12 14:3
 * 消息接收器，与某个通道进行绑定,Sink默认定义的消息通道
 * EnableBinding表示绑定Sink消息通道
 */
@EnableBinding(MyChannel.class)
public class MsgReceiver2 {
    public final static Logger logger= LoggerFactory.getLogger(MsgReceiver2.class);

    @StreamListener(MyChannel.INPUT)
    public void receive(Object payload){
        logger.info("Received:"+payload+":"+new Date());
    }
}

