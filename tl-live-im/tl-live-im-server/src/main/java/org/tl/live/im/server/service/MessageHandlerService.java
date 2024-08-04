package org.tl.live.im.server.service;

import jakarta.websocket.Session;
import org.springframework.stereotype.Service;
import org.tl.live.im.server.config.IMConstants;
import org.tl.live.im.server.manager.ConnectionManager;
import org.tl.live.im.server.protocal.GenericMessage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MessageHandlerService {
    Logger logger = Logger.getLogger(MessageHandlerService.class.getName());
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
        List<Session> roomAllUserConnect = ConnectionManager.getRoomAllUserConnect(roomId,userId);
        if(roomAllUserConnect.isEmpty()){
            return;
        }
        // 发送消息
        //TODO
        roomAllUserConnect.forEach(session -> {
            msgSend(session, message);
            logger.info("发送消息");
        });
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
}
