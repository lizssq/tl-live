package org.tl.live.busi;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class IMBusiapplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IMBusiapplication.class);
        springApplication.run(args);
    }
}
