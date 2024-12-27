package org.tl.live.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CustomServiceInstanceListSupplier {

    @Bean
    public ServiceInstanceListSupplier metadataFilteringSupplier(DiscoveryClient discoveryClient) {
        return new ServiceInstanceListSupplier() {
            @Override
            public String getServiceId() {
                return "tl-live-api";
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                return Flux.fromIterable(discoveryClient.getInstances("tl-live-api"))
                        .filter(instance -> "http".equals(instance.getMetadata().get("protocol")))
                        .collectList()
                        .flux();
            }
        };
    }
}