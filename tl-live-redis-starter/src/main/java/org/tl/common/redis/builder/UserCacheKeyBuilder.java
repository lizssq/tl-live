package org.tl.common.redis.builder;


import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCacheKeyBuilder extends RedisKeyBuilder{
    public static final String USER_PHONE = "user:phone:";
    public String getUserPhoneKey(String phone) {
        return super.getProfix() + USER_PHONE + phone;
    }
}
