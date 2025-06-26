package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName conversations
 */
@TableName(value ="conversations")
@Data
public class Conversations {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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

    @TableField(exist = false)
    private String toUserName;

    @TableField(exist = false)
    private String toUserAvatar;
}