package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName movie_favorite
 */
@TableName(value ="movie_favorite")
@Data
public class MovieFavorite {
    /**
     * 用户ID（逻辑外键关联User表）
     */
    @TableId
    private Long userId;

    /**
     * 电影ID（逻辑外键关联Movie表）
     */
    @TableId
    private Long movieId;

    /**
     * 收藏时间
     */
    private Date createTime;
}