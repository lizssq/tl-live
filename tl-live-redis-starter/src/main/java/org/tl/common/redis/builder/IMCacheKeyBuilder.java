package org.tl.common.redis.builder;

import org.springframework.context.annotation.Configuration;

@Configuration
public class IMCacheKeyBuilder extends RedisKeyBuilder{
    public static final String IM_TOKEN = "im:token:";
    public static final String IM_ROOM_USERS = "im:room:users:";
    public static final String IM_ROOM_CHAT = "im:room:chat:";
    public String getIMTokenKey(String userId) {
        return IM_TOKEN + userId;
    }

    public String getIMRoomUserKey(String roomId) {
        return IM_ROOM_USERS + roomId;
    }

    public String buildIMRoomChatKey(String roomId) {
        return IM_ROOM_CHAT + roomId;
    }
}
