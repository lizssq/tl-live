package org.tl.live.im.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IMServerApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IMServerApplication.class);

        springApplication.run(args);
    }
}
