package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 礼物配置表
 * @TableName gift_type
 */
@TableName(value ="gift_type")
@Data
public class GiftType {
    /**
     * 礼物ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物图片地址
     */
    private String imageUrl;

    /**
     * 金币价格
     */
    private Integer price;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 图片版本（用于缓存控制）
     */
    private Integer imageVersion;

    /**
     * 是否上架
     */
    private Integer isValid;

    /**
     * 创建时间
     */
    private Date createdAt;
}