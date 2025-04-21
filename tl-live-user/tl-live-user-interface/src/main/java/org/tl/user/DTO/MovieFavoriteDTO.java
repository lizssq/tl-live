package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MovieFavoriteDTO implements Serializable {
    /**
     * 用户ID（逻辑外键关联User表）
     */

    private Long userId;

    /**
     * 电影ID（逻辑外键关联Movie表）
     */

    private Long movieId;

    /**
     * 收藏时间
     */
    private Date createTime;
}