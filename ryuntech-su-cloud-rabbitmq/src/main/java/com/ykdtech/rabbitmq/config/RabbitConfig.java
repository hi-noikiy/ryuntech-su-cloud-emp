package com.ykdtech.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;


/**
 * @author wanqh
 * @date 2018/01/23 15:11
 * @description: RabbitMQ的配置类，用来配队列、交换器、路由等高级信息
 */
public class RabbitConfig {
    @Bean
    public Queue helloConfig(){
        return new Queue("hello");
    }
}
