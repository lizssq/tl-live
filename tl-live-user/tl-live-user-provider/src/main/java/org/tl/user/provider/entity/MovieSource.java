package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName movie_source
 */
@TableName(value ="movie_source")
@Data
public class MovieSource {
    /**
     * 资源ID
     */
    @TableId(type = IdType.AUTO)
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