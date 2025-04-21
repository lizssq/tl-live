package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tl.user.provider.entity.MovieSource;

/**
* @author k1341
* @description 针对表【movie_source】的数据库操作Mapper
* @createDate 2025-04-17 14:22:23
* @Entity org.tl.user.provider.entity.MovieSource
*/
@Mapper
@Repository
public interface MovieSourceMapper extends BaseMapper<MovieSource> {

    int deleteByPrimaryKey(Long id);

    int insert(MovieSource record);

    int insertSelective(MovieSource record);

    MovieSource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MovieSource record);

    int updateByPrimaryKey(MovieSource record);

    MovieSource findByMovieIdMovieSource(Long id);

}
