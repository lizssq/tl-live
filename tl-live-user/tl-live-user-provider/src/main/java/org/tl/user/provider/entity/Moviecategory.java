package org.tl.user.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName moviecategory
 */
@TableName(value ="moviecategory")
@Data
public class Moviecategory {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 分类名称（如动作、喜剧）
     */
    private String name;

    /**
     * 父分类ID（树形结构）
     */
    private Integer parentId;


    @TableField(exist = false)
    private Integer count;
}