package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.tl.user.DTO.RegionMovieCount;
import org.tl.user.provider.entity.MovieDO;

import java.util.List;
import java.util.Map;

/**
* @author k1341
* @description 针对表【movie】的数据库操作Mapper
* @createDate 2025-04-17 10:32:41
* @Entity org.tl.user.provider.entity.Movie
*/
@Mapper
@Repository
public interface MovieMapper extends BaseMapper<MovieDO> {

    int deleteByPrimaryKey(Long id);

    int insert(MovieDO record);

    int insertSelective(MovieDO record);

    MovieDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MovieDO record);

    int updateByPrimaryKey(MovieDO record);

    @Select("SELECT region AS `name`, COUNT(*) AS count FROM movie GROUP BY region ORDER BY region")
    List<Map<String, Object>> getRegionMovieCounts();

    List<RegionMovieCount> getReleaseYearMovieCounts();


}
