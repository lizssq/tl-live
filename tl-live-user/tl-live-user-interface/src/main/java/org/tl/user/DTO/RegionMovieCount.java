package org.tl.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class RegionMovieCount implements Serializable {
    private Integer id;         // 唯一标识
    private String name;  // 地区名
    private Integer count;         // 影片数量
}
