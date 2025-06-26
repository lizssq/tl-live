package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationStatusDTO implements java.io.Serializable {
    /**
     * 逻辑外键->users.id
     */
    private Integer userId;

    /**
     * 逻辑外键->conversations.id
     */
    private Integer conversationId;

    /**
     * 是否有未读消息
     */
    private Integer unreadCount;

    /**
     * 最后阅读时间
     */
    private Date lastReadTime;
    
    /**
     * 会话类型
     */
    private int ConversationType;
    
}