package org.tl.user.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class PaymentRecordDTO implements Serializable {
    /**
     * 交易ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID（逻辑外键 -> user.id）
     */
    private Integer userId;

    /**
     * 套餐ID（逻辑外键 -> recharge_plan.id）
     */
    private Integer planId;

    /**
     * 支付方式ID（逻辑外键 -> payment_method.id）
     */
    private Integer methodId;

    /**
     * 第三方支付流水号
     */
    private String transactionId;

    /**
     * 实付金额
     */
    private BigDecimal amountPaid;

    /**
     * 获得金币总数
     */
    private Integer coinsReceived;

    /**
     * 支付状态
     */
    private PaymentStatus paymentStatus;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 完成时间
     */
    private Date completedAt;


    private String methodName;

    private String planDesc;

    private String nickname;


    // 手动设置 paymentStatus 字段
    public void setPaymentStatus(String status) {
        this.paymentStatus = PaymentStatus.fromValue(status);
    }

}