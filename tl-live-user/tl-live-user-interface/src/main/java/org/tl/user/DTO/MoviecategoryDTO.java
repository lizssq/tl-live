package org.tl.user.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class MoviecategoryDTO implements Serializable {
    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称（如动作、喜剧）
     */
    private String name;

    /**
     * 父分类ID（树形结构）
     */
    private Integer parentId;


    private Integer count;
}