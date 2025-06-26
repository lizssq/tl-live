package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LiveRoomDTO implements Serializable {
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
     * 直播间描述
     */
    private String description;

    /**
     * 分类ID（逻辑外键关联Category表）
     */
    private Integer categoryId;

    /**
     * 封面图地址
     */
    private String coverUrl;

    /**
     * 直播状态（0: 未开播, 1: 直播中, 2: 已结束）
     */
    private Integer status;

    /**
     * 推流码
     */
    private String streamCode;

    /**
     * 推流地址
     */
    private String pushUrl;

    /**
     * 拉流地址（http）
     */
    private String pullUrlRtmp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private String nickname;

    private String avatar;

    private String categoryName;
}