package com.ryuntech.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 身份校验 auth
 *
 * @author antu
 * @date 2019-05-21
 */
@EnableFeignClients("com.ryuntech.*.api.service")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages={"com.ryuntech"})
public class RyunCloudAuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RyunCloudAuthorizationApplication.class, args);
    }

    @Autowired
    private RestTemplateBuilder builder;

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
}