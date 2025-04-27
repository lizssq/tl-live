package org.tl.user.provider.mapper;

import org.tl.user.DTO.RegionMovieCount;
import org.tl.user.provider.entity.Moviecategory;

import java.util.List;

/**
* @author k1341
* @description 针对表【moviecategory】的数据库操作Mapper
* @createDate 2025-04-17 11:15:59
* @Entity org.tl.user.provider.entity.Moviecategory
*/
public interface MoviecategoryMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Moviecategory record);

    int insertSelective(Moviecategory record);

    Moviecategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Moviecategory record);

    int updateByPrimaryKey(Moviecategory record);

    List<RegionMovieCount> getTopLevelCategoryCounts();

}
