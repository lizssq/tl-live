package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 直播分类表
 * @TableName live_category
 */
@TableName(value ="live_category")
@Data
public class LiveCategory {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;


    @TableField(exist = false)
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

    /**
     * 直播间数量
     */
    @TableField(exist = false)
    private int roomCount;
}