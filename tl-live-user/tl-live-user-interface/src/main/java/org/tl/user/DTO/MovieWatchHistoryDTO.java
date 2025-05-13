package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class MovieWatchHistoryDTO implements java.io.Serializable {
    /**
     * 用户ID
     */

    private Integer userId;

    /**
     * 电影ID
     */

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