package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class LiveRoomDTO implements Serializable {
    /**
     * 直播间ID
     */
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