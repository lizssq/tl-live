package org.tl.live.service;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.tl.common.redis.builder.IMCacheKeyBuilder;
import org.tl.live.config.IMConstants;
import org.tl.live.manager.ConnectionManager;
import org.tl.live.protocal.GenericMessage;
import org.tl.live.protocal.MessageBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.tl.live.manager.ConnectionManager.joinRoom;

@Service
public class MessageSendService {

    Logger logger = LoggerFactory.getLogger(MessageSendService.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IMCacheKeyBuilder imCacheKeyBuilder;

    public boolean publishNotice(String roomId, GenericMessage message) {
//        //TODO
//        List<Session> roomAllUserConnect = ConnectionManager.getRoomAllConnect(roomId);
//        if(roomAllUserConnect.isEmpty()){
//            return false;
//        }
//        // 发送消息
//        //TODO
//        GenericMessage genericMessage = generateNoticeMessage(roomId, message);
//        roomAllUserConnect.forEach(session -> {
//            msgSend(session, genericMessage);
//            logger.info("发送消息");
//        });
        rocketMQTemplate.convertAndSend("MESSAGE_CHAT", message);
        return true;
    }
    private void msgSend(Session session, GenericMessage message) {
        if(session.isOpen()){
            Object userId = session.getUserProperties().get(IMConstants.PROP_USER_ID);
            //TODO
            try {
                message.setFromUserId(Long.parseLong(userId.toString()));
                session.getBasicRemote().sendText(message.toString());
            } catch (IOException e) {
                logger.error("消息发送失败");
            }
        }

    }
    /**
     * 生成公告信息体
     *
     */
    public GenericMessage generateNoticeMessage(String roomId, String message) {
        ConnectionManager.joinRoom(roomId, "123");
        List<MessageBody> messageBodyList = new ArrayList<>();
        MessageBody messageBody = new MessageBody();
        messageBody.setContent(message);
        messageBodyList.add(messageBody);
        //TODO
        return new GenericMessage(3, Long.parseLong(roomId), 0L, messageBodyList);
    }

    public boolean pushChatMessage(String roomId, GenericMessage message) {
        //查看房间内所有用户
        String imRoomUserKey = imCacheKeyBuilder.getIMRoomUserKey(roomId);
        Set<String> allUsers = stringRedisTemplate.opsForSet().members(imRoomUserKey);
        //获取全部session连接
        allUsers.forEach(userId -> {
            //List<Session> roomAllConnect = ConnectionManager.getRoomAllConnect(roomId);
            Optional<Session> session = ConnectionManager.getSession(userId);
            if (session.isPresent()) {
                msgSend(session.get(), message);
                logger.info("发送消息给用户{}成功", userId);
            }
        });
        return true;
    }
}
