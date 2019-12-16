package com.ykdtech.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author EDZ
 * https://segmentfault.com/a/1190000012982159
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RabbitMqApplication {


    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args);
    }

}
