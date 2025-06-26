package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName conversation_status
 */
@TableName(value ="conversation_status")
@Data
public class ConversationStatus {
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
    @TableField(exist = false)
    private int ConversationType;


}