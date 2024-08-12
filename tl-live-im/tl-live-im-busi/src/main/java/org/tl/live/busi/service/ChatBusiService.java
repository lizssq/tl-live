package org.tl.live.busi.service;

import jakarta.annotation.Resource;
import org.apache.dubbo.common.constants.ClusterRules;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tl.common.redis.builder.IMCacheKeyBuilder;
import org.tl.live.config.IMConstants;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.live.inter.IIMRPCService;
import org.tl.live.protocal.GenericMessage;
import org.tl.live.protocal.MessageBody;

import java.util.List;
import java.util.Set;

@Service
public class ChatBusiService {
    //消息过滤、聚合、分发、持久化
    Logger logger= LoggerFactory.getLogger(ChatBusiService.class);
    @Resource
    private RedisTemplate<String, MessageBody> redisTemplate;

    @Value("${tl-live.im-busi.chat-max:5}")
    private int chatMax;

    @DubboReference(check = false,cluster = ClusterRules.BROADCAST)
    private IIMRPCService imrpcService;

    @DubboReference(check = false)
    private IGenerateIDRPCService generateIDRPCService;


    @Resource
    private IMCacheKeyBuilder imCacheKeyBuilder;

    public void handleChatMessage(GenericMessage message){
        String imRoomChatKey = imCacheKeyBuilder.buildIMRoomChatKey(message.getRoomId().toString());
        logger.info("处理聊天消息");
        Long roomId= message.getRoomId();
        if(message.getType().equals(IMConstants.MESSAGE_TYPE_GIFT)) {
            //私聊
            //消息持久化
            //消息分发
            //TODO 消息推送
            imrpcService.pushChatMessage(String.valueOf(message.getRoomId()),message);
        }else if(message.getType()==IMConstants.MESSAGE_TYPE_CHAT){

            //群聊
            //消息持久化
            //消息分发
            for(MessageBody body:message.getBody()){
                body.setFromUserId(message.getFromUserId());
                body.setFromUserName(message.getFromUserName());
                body.setMsgId(generateIDRPCService.getUnorderedID());
                redisTemplate.opsForSet().add(imRoomChatKey,body);
            }

            Set<MessageBody> messageBodies = redisTemplate.opsForSet().members(imRoomChatKey);
            if(messageBodies.size()>chatMax){
                //TODO 消息推送
                GenericMessage genericMessage = new GenericMessage();
                genericMessage.setRoomId(roomId);
                genericMessage.setType(IMConstants.MESSAGE_TYPE_CHAT);
                genericMessage.setBody(messageBodies.stream().toList());
                try{
                    imrpcService.pushChatMessage(String.valueOf(roomId),genericMessage);
                    logger.info("消息推送成功{}",genericMessage);
                }catch (Exception e){
                    logger.error("消息推送失败",e);
                }
                //清理redis缓存
                redisTemplate.opsForSet().remove(imRoomChatKey,messageBodies.toArray());
            }
        }
    }
}
