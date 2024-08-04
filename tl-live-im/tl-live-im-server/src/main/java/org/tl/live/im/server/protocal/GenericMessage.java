package org.tl.live.im.server.protocal;

import java.io.Serializable;
import java.util.List;

public class GenericMessage implements Serializable {
    /**
     *消息类型：
     */
    private Integer type;
    /**
     *房间
     ID

     */
    private Long roomId;
    /**
     *发送消息⽤户
     */
    private Long fromUserId;
    /**
     *消息体
     */
    private List<MessageBody> body;

    public GenericMessage(Integer type, Long roomId, Long fromUserId, List<MessageBody> body) {
        this.type = type;
        this.roomId = roomId;
        this.fromUserId = fromUserId;
        this.body = body;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", fromUserId=" + fromUserId +
                ", body=" + body +
                '}';
    }

    public GenericMessage() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public List<MessageBody> getBody() {
        return body;
    }

    public void setBody(List<MessageBody> body) {
        this.body = body;
    }
}