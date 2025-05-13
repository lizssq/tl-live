package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 直播打赏核心流水表
 * @TableName live_gift_record
 */
@TableName(value ="live_gift_record")
@Data
public class LiveGiftRecord {
    /**
     * 打赏记录ID
     */
    @TableId(type = IdType.AUTO)
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