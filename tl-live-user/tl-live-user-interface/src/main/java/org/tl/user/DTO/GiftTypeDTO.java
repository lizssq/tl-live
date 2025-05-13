package org.tl.user.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Data
@Getter
@Setter
public class GiftTypeDTO implements java.io.Serializable {
    /**
     * 礼物ID
     */

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