package org.tl.live.protocal;

import lombok.Data;

import java.io.Serializable;
@Data
public class MessageBody implements Serializable {
    private Long msgId;
    private String content;
    private Long toUserId;
    private Long fromUserId;
    private String fromUserName;
    private Long giftId;
    private int giftCount;
    private String giftImage;
    private String giftName;
    private String conversationId;
}