package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName live_room
 */
@TableName(value ="live_room")
@Data
public class LiveRoom {
    /**
     * 直播间ID
     */
    @TableId(type = IdType.AUTO)
    private Long roomId;

    /**
     * 主播ID（逻辑外键关联User表）
     */
    private Long userId;

    /**
     * 直播间标题
     */
    private String title;

    /**
     * 分类ID（逻辑外键关联Category表）
     */
    private Integer categoryId;

    /**
     * 封面地址
     */
    private String coverUrl;

    /**
     * 状态（0: 未开播, 1: 直播中）
     */
    private Integer status;

    /**
     * 直播源地址
     */
    private String streamUrl;
}