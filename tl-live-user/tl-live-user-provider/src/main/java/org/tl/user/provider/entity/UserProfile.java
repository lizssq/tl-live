package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user_profile
 */
@TableName(value ="user_profile")
@Data
public class UserProfile {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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