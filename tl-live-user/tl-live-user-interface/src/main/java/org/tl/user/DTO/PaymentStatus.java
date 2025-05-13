package org.tl.user.DTO;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum PaymentStatus implements Serializable {
    PENDING("pending"),
    SUCCESS("success"),
    FAILED("failed");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    // 可选：根据数据库值反序列化为枚举
    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.status.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知支付状态: " + value);
    }
}