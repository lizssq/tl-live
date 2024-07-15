package org.tl.user.provider.config;

import java.util.concurrent.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolManager {
    public static ThreadPoolExecutor threadPoolManager = new ThreadPoolExecutor(
            2, 8, 3, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactory(){
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("threadPoolManager-"+ ThreadLocalRandom.current().nextInt(1000));
                    return thread;
                }

            }
    );
}
