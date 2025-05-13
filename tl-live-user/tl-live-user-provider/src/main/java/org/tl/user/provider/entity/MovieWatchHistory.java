package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户观影记录表
 * @TableName movie_watch_history
 */
@TableName(value ="movie_watch_history")
@Data
public class MovieWatchHistory {
    /**
     * 用户ID
     */
    @TableId
    private Integer userId;

    /**
     * 电影ID
     */
    @TableId
    private Integer movieId;

    /**
     * 最后观看时间
     */
    private Date lastWatchedAt;

    /**
     * 观看进度（秒）
     */
    private Integer progressSeconds;
}