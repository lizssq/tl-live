package org.tl.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
@MapperScan("org.tl.user.provider.mapper")
public class UserProvideApplication {
    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(UserProvideApplication.class);
        springApplication.run(args);
    }
}
