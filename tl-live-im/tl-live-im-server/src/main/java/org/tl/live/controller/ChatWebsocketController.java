package org.tl.live.controller;

import com.alibaba.fastjson.JSON;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.tl.live.config.IMConstants;
import org.tl.live.manager.ChannelIdleStateManager;
import org.tl.live.manager.ConnectionManager;
import org.tl.live.manager.MessageTypeDispatchManager;
import org.tl.live.protocal.GenericMessage;
import org.tl.live.uti.SpringContextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@ServerEndpoint("/chat/{userId}")
public class ChatWebsocketController {
    Logger logger = LoggerFactory.getLogger(ChatWebsocketController.class);
    private final static List<Session> sessionList = new ArrayList<>();

    private Session session;

    private String userId;

    private MessageTypeDispatchManager messageTypeDispatchManager;

    private ChannelIdleStateManager channelIdleStateManager;
    /**
     * 1 建立连接
     * 2 连接错误
     * 3 关闭连接
     * 4 收到消息
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        logger.info("建立连接");
        //sessionList.add(session);
        try{
            Long lUserId= Long.valueOf(userId);
            logger.info("用户id："+lUserId);
            session.getUserProperties().put(IMConstants.PROP_USER_ID,lUserId);

            //心跳管理,开始连接
            channelIdleStateManager= SpringContextUtil.getBean(ChannelIdleStateManager.class);
            channelIdleStateManager.connect(userId,session);
            logger.info("用户id以缓存到session中");
        }catch (Exception e){
            logger.error("用户id不是数字,用户未登录");
        }
        if(ConnectionManager.register(userId,session)){
            logger.info("用户连接成功，用户id："+userId);
        }else {
            logger.info("用户连接失败，重复登录，用户id"+userId);
        }
        this.session = session;
        this.userId = userId;
    }

    @OnClose
    public void onClose(){
        logger.info("关闭连接");
        //sessionList.remove(session);
        ConnectionManager.cancel(this.userId,session);
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        logger.info("收到消息："+message);

        //心跳管理,更新最后读取时间
        if(channelIdleStateManager==null){
            channelIdleStateManager= SpringContextUtil.getBean(ChannelIdleStateManager.class);
        }
        channelIdleStateManager.read(session);
        if(session.getUserProperties().containsKey(IMConstants.PROP_USER_ID)){
            Object userId = session.getUserProperties().get(IMConstants.PROP_USER_ID);

            //按JOSN格式解析消息
            GenericMessage genericMessage= JSON.parseObject(message, GenericMessage.class);
            //GenericMessage genericMessage = new GenericMessage();
            messageTypeDispatchManager= SpringContextUtil.getBean(MessageTypeDispatchManager.class);
            messageTypeDispatchManager.messageTypeDispatch(userId.toString(),genericMessage);

//            for(Session s:ConnectionManager.getAllSession()){
//                if(s.isOpen()){
//                    s.getBasicRemote().sendText("用户"+userId+"说："+message);
//                }
//            }
        } else{
            if(session.isOpen()){
                Optional<Session> optSession = ConnectionManager.getSession(this.userId);
                if(optSession.isPresent()){
                    optSession.get().getBasicRemote().sendText("用户未登录,请先登录");
                }
                //session.getBasicRemote().sendText("用户未登录,请先登录");
            }
            //session.getBasicRemote().sendText("用户未登录,请先登录");
        }
        //session.getBasicRemote().sendText("收到消息："+message);

    }

    @OnError
    public void onError(Throwable error){
        logger.info("连接错误,错误信息："+error.getMessage());
    }


}
