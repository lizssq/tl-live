package org.tl.user.provider.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Bill {
    private LocalDate date;
    private LocalTime time;
    private String type;       // 充值/消费/收入
    private String description;
    private BigDecimal amount;
}