package org.tl.user.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
public class GiftLogDTO implements java.io.Serializable {
    /**
     * 打赏记录ID
     */
    private Long logId;

    /**
     * 打赏用户ID（逻辑外键关联User表）
     */
    private Long senderId;

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


    private String senderName;

    /**
     * 主播昵称
     */

    private String receiverName;

    /**
     * 礼物名称
     */

    private String giftName;

}