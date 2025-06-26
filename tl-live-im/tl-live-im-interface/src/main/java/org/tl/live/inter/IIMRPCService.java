package org.tl.live.inter;


import org.tl.live.protocal.GenericMessage;

public interface IIMRPCService {
    public String generateIMToken(String userId);

    public boolean checkIMToken(String userId,String token);

    //发布公告
    public boolean publishNotice(String roomId, GenericMessage message);

    public boolean pushChatMessage(String roomId, GenericMessage message);

    public boolean pushPrivateChatMessage(String userId, GenericMessage message);
}
