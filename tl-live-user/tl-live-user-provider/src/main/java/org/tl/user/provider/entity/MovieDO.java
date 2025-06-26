package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName movie
 */
@TableName(value ="movie")
@Data
public class MovieDO {
    /**
     * 电影ID
     */
    @TableId(type = IdType.AUTO)
    private Long movieId;

    /**
     * 电影标题
     */
    private String title;

    /**
     * 导演
     */
    private String director;

    /**
     * 主演（逗号分隔）
     */
    private String actors;

    /**
     * 剧情简介
     */
    private String description;

    /**
     * 封面图地址
     */
    private String coverUrl;

    /**
     * 时长（分钟）
     */
    private Integer duration;

    /**
     * 上映年份
     */
    private int releaseYear;

    /**
     * 地区（如中国大陆）
     */
    private String region;

    /**
     * 语言
     */
    private String language;

    /**
     * 评分（0-10分）
     */
    private BigDecimal rating;

    /**
     * 入库时间
     */
    private Date createTime;

    private String parentCategoryName;

    @TableField(exist = false)
    private List<String> category;

    @TableField(exist = false)
    private Date favoriteTime;

    /**
     * 观看进度（秒）
     */
    @TableField(exist = false)
    private Integer progressSeconds;

    /**
     * 最后观看时间
     */
    @TableField(exist = false)
    private Date lastWatchedAt;

    @TableField(exist = false)
    private String playUrl; // 视频地址，可能用于播放或下载链接
}