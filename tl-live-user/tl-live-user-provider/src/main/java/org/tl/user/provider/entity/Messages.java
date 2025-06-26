package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName messages
 */
@TableName(value ="messages")
@Data
public class Messages {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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