package org.tl.live.im.server.protocal;

import java.io.Serializable;

public class MessageBody implements Serializable {
    private Long msgId;
    private String content;
    private Long toUserId;
    private Long fromUserId;
    private String fromUserName;

    @Override
    public String toString() {
        return "MessageBody{" +
                "msgId=" + msgId +
                ", content='" + content + '\'' +
                ", toUserId=" + toUserId +
                ", fromUserId=" + fromUserId +
                ", fromUserName='" + fromUserName + '\'' +
                '}';
    }

    public MessageBody(Long msgId, String content, Long toUserId, Long fromUserId, String fromUserName) {
        this.msgId = msgId;
        this.content = content;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
    }

    public MessageBody() {
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
}