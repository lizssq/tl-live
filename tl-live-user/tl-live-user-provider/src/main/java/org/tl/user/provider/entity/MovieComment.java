package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName movie_comment
 */
@TableName(value ="movie_comment")
@Data
public class MovieComment {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long commentId;

    /**
     * 用户ID（逻辑外键关联User表）
     */
    private Long userId;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatar;

    /**
     * 电影ID（逻辑外键关联Movie表）
     */
    private Long movieId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 用户评分（1-5星）
     */
    private Integer rating;

    /**
     * 审核状态（0: 待审核, 1: 通过）
     */
    private Integer status;

    /**
     * 评论时间
     */
    private Date createTime;
}