package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserFollowDTO implements java.io.Serializable {
    /**
     * 主键
     */
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
    private String userName;

    /**
     * 被关注昵称
     */
    private String targetName;
    
    /**
     * 关注用户头像
     */
    private String userAvatar;
    
    /**
     * 被关注用户头像
     */
    private String targetAvatar;

    /**
     * 简介
     */
    private String bio;
    
}