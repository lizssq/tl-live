package org.tl.user.provider.util;

import org.springframework.stereotype.Component;

@Component
public class MobileRedisKeyBuilder {
    public static final String MOBILE_CODE = "mobile:code:";

    public static String getMobileCodeKey(String mobile) {
        return MOBILE_CODE + mobile;
    }
}
