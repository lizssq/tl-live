package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;
@Data
public class PaymentMethodDTO implements Serializable {
    /**
     * 支付方式ID
     */
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