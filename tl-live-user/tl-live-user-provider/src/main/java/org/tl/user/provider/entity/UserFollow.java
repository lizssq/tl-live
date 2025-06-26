package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户关注关系表
 * @TableName user_follow
 */
@TableName(value ="user_follow")
@Data
public class UserFollow {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关注方用户ID
     */
    private Integer userId;

    /**
     * 被关注方用户ID
     */
    private Integer targetId;

    /**
     * 关注时间
     */
    private Date createdAt;

    /**
     * 关注昵称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 被关注昵称
     */
    @TableField(exist = false)
    private String targetName;

    /**
     * 关注用户头像
     */
    @TableField(exist = false)
    private String userAvatar;

    /**
     * 被关注用户头像
     */
    @TableField(exist = false)
    private String targetAvatar;

    /**
     * 简介
     */
    @TableField(exist = false)
    private String bio;

}