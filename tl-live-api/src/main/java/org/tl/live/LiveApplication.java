package org.tl.live;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDubbo
@EnableDiscoveryClient
public class LiveApplication {
    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(LiveApplication.class);


        springApplication.run(args);
    }
}
