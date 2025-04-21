package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName movie_source
 */
@Data
public class MovieSourceDTO implements Serializable {
    /**
     * 资源ID
     */
    private Long sourceId;

    /**
     * 电影ID（逻辑外键关联Movie表）
     */
    private Long movieId;

    /**
     * 播放地址
     */
    private String playUrl;

    /**
     * 文件格式（如MP4）
     */
    private String fileFormat;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 是否默认源（0: 否, 1: 是）
     */
    private Integer isDefault;
}