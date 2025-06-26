package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MessagesDTO implements java.io.Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 逻辑外键->conversations.id
     */
    private Integer conversationId;

    /**
     * 逻辑外键->users.id
     */
    private Integer senderId;

    /**
     * 
     */
    private String content;

    /**
     * 内容类型：0=文本 1=图片 2=文件
     */
    private Integer contentType;

    /**
     * 
     */
    private Date sentAt;

    /**
     * 已读时间
     */
    private Date readAt;
}