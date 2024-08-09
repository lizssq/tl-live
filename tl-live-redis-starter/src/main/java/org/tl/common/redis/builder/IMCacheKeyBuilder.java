package org.tl.common.redis.builder;

import org.springframework.context.annotation.Configuration;

@Configuration
public class IMCacheKeyBuilder extends RedisKeyBuilder{
    public static final String IM_TOKEN = "im:token:";
    public String getIMTokenKey(String userId) {
        return IM_TOKEN + userId;
    }
}
