package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
public enum PaymentStatus {

    PENDING("pending"),
    SUCCESS("success"),
    FAILED("failed");

    @EnumValue
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