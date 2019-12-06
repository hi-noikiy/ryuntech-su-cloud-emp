package com.ryuntech.saas;

import com.ryuntech.saas.api.helper.EnableRyunResourceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author EDZ
 */
@EnableRyunResourceServer
@EnableFeignClients("com.ryuntech.authorization.api")
@EnableEurekaClient
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.ryuntech"})
@EnableTransactionManagement
public class RyunCloudServiceSaasApplication {

    public static void main(String[] args) {
        SpringApplication.run(RyunCloudServiceSaasApplication.class, args);
    }

    @Autowired
    private RestTemplateBuilder builder;

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
}
