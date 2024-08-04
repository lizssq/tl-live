package org.tl.live.im.server.manager;

import io.micrometer.common.util.StringUtils;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Author
 ：
 roy
 * Description
 ：

 连接管理器  **/
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private static final Map<String, Session> CHANNEL_CONTAINER = new ConcurrentHashMap<>();

    //存放房间与⽤户关系key为RoomId, value为Set<UserId>
    private static final Map<String, Set<String>> ROOM_CONTAINER = new ConcurrentHashMap<>();

    public static boolean register(String sessionId, Session session) {
        Session addedSession = CHANNEL_CONTAINER.putIfAbsent(sessionId, session);
        if (null != addedSession) {
            logger.warn("sessionId:{} 已存在，不允许重复注册 ", sessionId);
            return false;
        }
        return true;
    }

    public static Optional<Session> getSession(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(CHANNEL_CONTAINER.get(sessionId));
    }

    public static void cancel(String sessionId, Session session) {
        Optional<Session> optConn = getSession(sessionId);
        if (optConn.isPresent()) {
            if (optConn.get().getId().equals(session.getId())) {
                CHANNEL_CONTAINER.remove(sessionId);
                logger.debug(" 清理路由成功,sessionId=>{}", sessionId);
            }
        }
    }

    public static List<Session> getAllSession() {
        return CHANNEL_CONTAINER.values().stream().toList();
    }

    /**
     * 进入房间
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     */
    public static void joinRoom(String roomId, String userId) {
        ROOM_CONTAINER.computeIfAbsent(roomId, k -> new ConcurrentSkipListSet<>()).add(userId);
    }

    /**
     * 退出房间
     *
     * @param roomId 房间ID
     * @param userId 用户ID
     */
    public static void exitRoom(String roomId, String userId) {
        Set<String> roomUsers = ROOM_CONTAINER.get(roomId);
        if (!CollectionUtils.isEmpty(roomUsers)) {
            roomUsers.remove(userId);
        }
    }

    /**
     * 获取房间内所有用户连接
     * 自己必须在这个房间里
     *
     * @param roomId 房间
     * @param userId 消息发送者ID
     * @return List<Session>
     */
    public static List<Session> getRoomAllUserConnect(String roomId, String userId) {
        Set<String> userSet = ROOM_CONTAINER.get(roomId);
        // 房间没人，返回空
        if (userSet == null || userSet.isEmpty()) {
            return Collections.emptyList();
        }
        // 自己不在房间里，不能向房间里的人发消息，返回空。
        if (!userSet.contains(userId)) {
            return Collections.emptyList();
        }

        LinkedList<Session> resultList = new LinkedList<>();
        userSet.forEach(u -> {
            Optional<Session> optSession = getSession(u);
            optSession.ifPresent(resultList::add);
        });
        return resultList;
    }
}