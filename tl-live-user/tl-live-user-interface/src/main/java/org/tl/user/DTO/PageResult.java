package org.tl.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements java.io.Serializable {
    private Integer pageNum;    // 当前页码
    private Integer pageSize;   // 每页数量
    private Long total;         // 总记录数
    private Integer pages;      // 总页数
    private List<T> list;       // 数据列表

    // getter/setter...
}