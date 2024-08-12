package org.tl.live.busi.consumer;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tl.live.busi.service.ChatBusiService;
import org.tl.live.config.IMConstants;
import org.tl.live.protocal.GenericMessage;


@Component
@RocketMQMessageListener(topic = IMConstants.MESSAGE_CHAT, consumerGroup = "tl-live-im-busi")
public class chatMessageConsumer implements RocketMQListener<GenericMessage> {

    Logger logger = LoggerFactory.getLogger(chatMessageConsumer.class);

    @Resource
    private ChatBusiService chatBusiService;

    @Override
    public void onMessage(GenericMessage s) {
        System.out.println("Received message: " + s);
        logger.info("Received message: " + s);
        chatBusiService.handleChatMessage(s);
    }
}
