package org.tl.user.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class UserProfileDTO implements java.io.Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户ID（逻辑外键关联User表）
     */
    private Long userId;

    /**
     * 性别（0: 未知）
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 余额
     */
    private BigDecimal balance;

    private String nickname;
}