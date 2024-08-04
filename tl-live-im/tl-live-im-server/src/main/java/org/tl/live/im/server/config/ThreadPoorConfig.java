package org.tl.live.im.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoorConfig {
    @Value("${tllive.im.thread.poor.corePoolSize:2}")
    private int corePoolSize;
    @Value("${tllive.im.thread.poor.maxPoolSize:8}")
    private int maxPoolSize;
    @Value("${tllive.im.thread.poor.queueCapacity:10}")
    private int queueCapacity;
    @Value("${tllive.im.thread.poor.keepAliveSeconds:30}")
    private int keepAliveSeconds;
    @Bean("asyncExecutor")
    public Executor threadPoorExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setThreadNamePrefix("customThreadPoorExecutor");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return taskExecutor;
    }

}
