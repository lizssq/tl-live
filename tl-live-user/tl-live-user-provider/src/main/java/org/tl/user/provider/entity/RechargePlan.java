package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 充值商品配置表
 * @TableName recharge_plan
 */
@TableName(value ="recharge_plan")
@Data
public class RechargePlan {
    /**
     * 套餐ID
     */
    @TableId(type = IdType.AUTO)
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