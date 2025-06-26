package org.tl.live.service;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.tl.live.config.IMConstants;
import org.tl.live.manager.ConnectionManager;
import org.tl.live.protocal.GenericMessage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MessageHandlerService {
    Logger logger = Logger.getLogger(MessageHandlerService.class.getName());


    @Resource
    private RocketMQTemplate rocketMQTemplate;
    public void sendIndexMessage(String userId, String roomId) {
        Optional<Session> conQpt = ConnectionManager.getSession(userId);
        if(conQpt.isEmpty()){
            return;
        }
        // 发送最近历史消息
        //TODO
        logger.info("发送最近历史消息");
        // 发送房间公告
        //TODO
        logger.info("发送房间公告");
    }

    public void sendRoomChatMessage(String userId, String roomId, GenericMessage message) {
//        List<Session> roomAllUserConnect = ConnectionManager.getRoomAllUserConnect(roomId,userId);
//        if(roomAllUserConnect.isEmpty()){
//            return;
//        }
//        // 发送消息
//        //TODO
//        roomAllUserConnect.forEach(session -> {
//            msgSend(session, message);
//            logger.info("发送消息");
//        });
        message.setRoomId(Long.parseLong(roomId));
        message.setFromUserId(Long.parseLong(userId));
        rocketMQTemplate.convertAndSend(IMConstants.MESSAGE_CHAT, message);
    }

    private void msgSend(Session session, GenericMessage message) {
        if(session.isOpen()){
            Object userId = session.getUserProperties().get(IMConstants.PROP_USER_ID);
            //TODO
            try {
                message.setFromUserId(Long.parseLong(userId.toString()));
                session.getBasicRemote().sendText(message.toString());
            } catch (IOException e) {
                logger.warning("消息发送失败");
            }
        }

    }

    public void sendPrivateChatMessage(String userId, GenericMessage message) {
        Optional<Session> conQpt = ConnectionManager.getSession(userId);
        if(conQpt.isEmpty()){
            return;
        }

        message.setFromUserId(Long.parseLong(userId));
        message.setRoomId(null);

        rocketMQTemplate.convertAndSend(IMConstants.MESSAGE_CHAT, message);

    }
}
