package org.tl.user.provider.util;

import org.springframework.stereotype.Service;

@Service
public class UserRedisKeyBuilder {
    public static final String USER_INFO = "user:info:";

    public static String getUserInfoKey(Long userId) {
        return USER_INFO + userId;
    }
}
