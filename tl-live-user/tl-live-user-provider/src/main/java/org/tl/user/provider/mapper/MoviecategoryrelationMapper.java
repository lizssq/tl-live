package org.tl.user.provider.mapper;

import org.tl.user.provider.entity.Moviecategoryrelation;

import java.util.List;

/**
* @author k1341
* @description 针对表【moviecategoryrelation】的数据库操作Mapper
* @createDate 2025-04-17 11:16:16
* @Entity org.tl.user.provider.entity.Moviecategoryrelation
*/
public interface MoviecategoryrelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Moviecategoryrelation record);

    int insertSelective(Moviecategoryrelation record);

    Moviecategoryrelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Moviecategoryrelation record);

    int updateByPrimaryKey(Moviecategoryrelation record);

    List<String> findCategoriesByMovieId(long id);
}
