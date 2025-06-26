package org.tl.live.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.live.inter.IIMRPCService;
import org.tl.live.protocal.GenericMessage;
import org.tl.live.service.IMTokenService;
import org.tl.live.service.MessageSendService;

@DubboService
public class IMRPCService implements IIMRPCService {
    @Resource
    private IMTokenService imTokenService;

    @Resource
    private MessageSendService messageSendService;
    @Override
    public String generateIMToken(String userId) {
        return imTokenService.generateIMToken(userId);
    }

    @Override
    public boolean checkIMToken(String userId, String token) {
        return imTokenService.checkIMToken(userId, token);
    }

    @Override
    public boolean publishNotice(String roomId, GenericMessage message) {
        return messageSendService.publishNotice(roomId, message);
    }

    @Override
    public boolean pushChatMessage(String roomId, GenericMessage message) {
        return messageSendService.pushChatMessage(roomId, message);
    }

    @Override
    public boolean pushPrivateChatMessage(String userId, GenericMessage message) {
        return messageSendService.pushPrivateChatMessage(userId, message);
    }
}
