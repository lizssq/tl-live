package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationsDTO implements java.io.Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 会话类型：0=私信，1=系统通知
     */
    private Integer conversationType;

    /**
     * 私信参与者1（逻辑外键->users.id）
     */
    private Integer user1Id;

    /**
     * 私信参与者2（逻辑外键->users.id）
     */
    private Integer user2Id;

    /**
     * 系统通知接收者（逻辑外键->users.id）
     */
    private Integer receiverId;

    /**
     * 最后消息摘要
     */
    private String preview;

    /**
     * 最新消息时间
     */
    private Date lastMessageTime;

    /**
     * 
     */
    private Date createdAt;


    private String toUserName;

    private String toUserAvatar;
}