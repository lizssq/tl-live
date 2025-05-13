package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import lombok.Data;

/**
 * 支付流水记录表
 * @TableName payment_record
 */
@TableName(value ="payment_record")
@Data
public class PaymentRecord {
    /**
     * 交易ID
     */
    @TableId(type = IdType.INPUT)
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

    @TableField(exist = false)
    private String methodName;
    @TableField(exist = false)
    private String planDesc;
    @TableField(exist = false)
    private String nickname;

    // 手动设置 paymentStatus 字段
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = PaymentStatus.fromValue(paymentStatus);
    }
}