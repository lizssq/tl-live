package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName moviecategoryrelation
 */
@TableName(value ="moviecategoryrelation")
@Data
public class Moviecategoryrelation {
    /**
     * 电影ID（逻辑外键关联Movie表）
     */
    @TableId
    private Long movieId;

    /**
     * 分类ID（逻辑外键关联MovieCategory表）
     */
    @TableId
    private Integer categoryId;
}