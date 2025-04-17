package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class MovieDTO implements Serializable {
    // 电影ID（数据库自增，不需要在构造函数中赋值）
    private Long movieId;

    // 电影标题
    private String title;

    // 导演
    private String director;

    // 主演（逗号分隔）
    private String actors;

    // 剧情简介
    private String description;

    // 封面图地址
    private String coverUrl;

    // 时长（分钟）
    private int duration;

    // 上映年份
    private int releaseYear; // 或者使用 java.sql.Year

    // 地区（如中国大陆）
    private String region;

    // 语言
    private String language;

    // 评分（0-10分）
    private BigDecimal rating;

    // 入库时间
    private Date createTime;

    private List<String> category;



}
