package org.tl.live.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.tl.live.config.IMConstants;
import org.tl.live.protocal.GenericMessage;
import org.tl.live.service.MessageHandlerService;

import java.util.concurrent.Executor;


@Component
public class MessageTypeDispatchManager {

    private Logger logger = LoggerFactory.getLogger(MessageTypeDispatchManager.class);

    // 注入消息处理服务
    @Resource
    private MessageHandlerService messageHandlerService;

    // 注入异步执行器
    @Resource(name = "asyncExecutor")
    private Executor executor;

    /**
     * 根据UserID，对消息进行路由。
     *
     * @param userId 用户ID
     * @param message 消息对象
     */
    public void messageTypeDispatch(String userId, GenericMessage message) {
        if (message.getType() == null) {
            logger.warn("消息格式异常，直接丢弃");
            return;
        }
        String roomId = message.getRoomId().toString();
        switch (message.getType()) {
            case IMConstants.MESSAGE_TYPE_JOIN_ROOM: // 加入房间
                ConnectionManager.joinRoom(roomId, userId);
                logger.info("用户=>{},加入房间=>{}",
                        userId, roomId);
                executor.execute(() -> messageHandlerService.sendIndexMessage(userId, roomId));
                break;
            case IMConstants.MESSAGE_TYPE_EXIT_ROOM: // 退出房间
                ConnectionManager.exitRoom(roomId, userId);
                logger.info("用户=>{},{}退出房间=>{}",
                        userId, roomId);
                break;
            case IMConstants.MESSAGE_TYPE_CHAT: // 聊天
                executor.execute(() -> messageHandlerService.sendRoomChatMessage(userId, roomId, message));
                logger.info("用户=>{},房间=>{},{}发送消息=>{}", userId, roomId, message);
                break;
            default:
                logger.warn("消息类型异常, message =>{}", message);
        }
    }
}
