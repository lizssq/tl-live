package org.tl.user.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RechargePlanDTO implements java.io.Serializable {
    /**
     * 套餐ID
     */
    private Integer id;

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 基础金币数
     */
    private Integer baseCoins;

    /**
     * 赠送金币数
     */
    private Integer bonusCoins;

    /**
     * 是否上架
     */
    private Integer isActive;

    /**
     * 套餐描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createdAt;
}