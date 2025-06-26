package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LiveCategoryDTO implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;


    private Integer count;

    /**
     * 分类标识（英文/拼音唯一）
     */
    private String slug;

    /**
     * 分类简介
     */
    private String description;

    /**
     * 分类封面图
     */
    private String coverUrl;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 排序，越小越靠前
     */
    private Integer sortOrder;

    /**
     * 是否展示：1=展示，0=隐藏
     */
    private Integer isVisible;

    /**
     * 父级分类 ID，0 表示顶级
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private int roomCount;
}