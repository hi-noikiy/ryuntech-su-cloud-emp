package com.ykdtech.rabbitmq.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * @author wanqh
 * @date 2018/01/23 14:56
 * @description: 消息生产者Sender使用AmqpTemplate接口的实例来实现消息的发送
 */
@Component
public class Sender {
    private final Logger logger = LoggerFactory.getLogger(Sender.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sender(){
        String context = "wqh say hello " + new Date();
        logger.info("发送消息=========》》》》{}",context);
        this.amqpTemplate.convertAndSend("hello",context);
    }
}
