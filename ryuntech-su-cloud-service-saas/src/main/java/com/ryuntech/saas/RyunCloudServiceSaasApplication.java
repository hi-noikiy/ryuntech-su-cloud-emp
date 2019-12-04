package com.ryuntech.saas;

import com.ryuntech.saas.api.helper.EnableRyunResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author EDZ
 */
@EnableRyunResourceServer
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
@SpringBootApplication(scanBasePackages={"com.ryuntech"})
@EnableTransactionManagement
public class RyunCloudServiceSaasApplication {

    public static void main(String[] args) {
        SpringApplication.run(RyunCloudServiceSaasApplication.class, args);
    }
}
