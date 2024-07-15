package org.tl.common.redis.builder;

import org.springframework.beans.factory.annotation.Value;

public class RedisKeyBuilder {
    @Value("${spring.application.name}")
    private String appName;

    private String s=":";
    public String getProfix() {
        return appName+s;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
