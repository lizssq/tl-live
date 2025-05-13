package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName gift_log
 */
@TableName(value ="gift_log")
@Data
public class GiftLog {
    /**
     * 打赏记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * 打赏用户ID（逻辑外键关联User表）
     */
    private Long senderId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 主播ID（逻辑外键关联User表）
     */
    private Long receiverId;

    /**
     * 礼物ID（逻辑外键关联GiftType表）
     */
    private Integer giftId;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 总花费
     */
    private BigDecimal totalCost;

    /**
     * 打赏时间
     */
    private Date time;

    /**
     * 打赏用户昵称
     */
    @TableField(exist = false)
    private String senderName;

    /**
     * 主播昵称
     */
    @TableField(exist = false)
    private String receiverName;

    /**
     * 礼物名称
     */
    @TableField(exist = false)
    private String giftName;
}