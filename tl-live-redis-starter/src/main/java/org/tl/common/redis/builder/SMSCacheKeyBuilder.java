package org.tl.common.redis.builder;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSCacheKeyBuilder extends RedisKeyBuilder{
    public static final String MOBILE_CODE = "sms:login:code:";

    public String getMobileCodeKey(String mobile) {
        return getProfix() + MOBILE_CODE + mobile;
    }
}
