package org.tl.live.service;

import jakarta.websocket.Session;
import org.springframework.stereotype.Service;
import org.tl.live.config.IMConstants;
import org.tl.live.manager.ConnectionManager;
import org.tl.live.protocal.GenericMessage;
import org.tl.live.protocal.MessageBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MessageSendService {

    Logger logger = Logger.getLogger(MessageSendService.class.getName());

    public boolean publishNotice(String roomId, String message) {
        //TODO
        List<Session> roomAllUserConnect = ConnectionManager.getRoomAllConnect(roomId);
        if(roomAllUserConnect.isEmpty()){
            return false;
        }
        // 发送消息
        //TODO
        GenericMessage genericMessage = generateNoticeMessage(roomId, message);
        roomAllUserConnect.forEach(session -> {
            msgSend(session, genericMessage);
            logger.info("发送消息");
        });
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
                logger.warning("消息发送失败");
            }
        }

    }
    /**
     * 生成公告信息体
     *
     */
    public GenericMessage generateNoticeMessage(String roomId, String message) {
        List<MessageBody> messageBodyList = new ArrayList<>();
        MessageBody messageBody = new MessageBody();
        messageBody.setContent(message);
        messageBodyList.add(messageBody);
        //TODO
        return new GenericMessage(3, Long.parseLong(roomId), 0L, messageBodyList);
    }
}
