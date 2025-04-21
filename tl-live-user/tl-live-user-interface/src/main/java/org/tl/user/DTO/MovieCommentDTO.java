package org.tl.user.DTO;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MovieCommentDTO implements Serializable {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 用户ID（逻辑外键关联User表）
     */
    private Long userId;

    private String nickName;

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