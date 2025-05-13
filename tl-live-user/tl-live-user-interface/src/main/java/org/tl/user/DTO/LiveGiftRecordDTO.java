package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class LiveGiftRecordDTO implements java.io.Serializable {
    /**
     * 打赏记录ID
     */
    private Long id;

    /**
     * 打赏用户ID（逻辑外键 -> user.id）
     */
    private Integer senderId;

    /**
     * 主播用户ID（逻辑外键 -> user.id）
     */
    private Integer anchorId;

    /**
     * 直播间ID（逻辑外键 -> live_room.id）
     */
    private String liveId;

    /**
     * 礼物类型ID（逻辑外键 -> gift_type.id）
     */
    private Integer giftId;

    /**
     * 消耗金币数
     */
    private Integer coinAmount;

    /**
     * 礼物数量
     */
    private Integer giftCount;

    /**
     * 总金币数
     */
    private Integer totalCoins;

    /**
     * 附加留言
     */
    private String message;

    /**
     * 打赏时间
     */
    private Date createdAt;
}