package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 支付渠道配置表
 * @TableName payment_method
 */
@TableName(value ="payment_method")
@Data
public class PaymentMethod {
    /**
     * 支付方式ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 支付名称
     */
    private String methodName;

    /**
     * 图标组件名
     */
    private String iconName;

    /**
     * 是否启用
     */
    private Integer isAvailable;
}