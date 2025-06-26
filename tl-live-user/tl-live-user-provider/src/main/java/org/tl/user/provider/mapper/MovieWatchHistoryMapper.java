package org.tl.user.provider.mapper;

import org.tl.user.provider.entity.MovieDO;
import org.tl.user.provider.entity.MovieWatchHistory;

import java.util.List;

/**
* @author k1341
* @description 针对表【movie_watch_history(用户观影记录表)】的数据库操作Mapper
* @createDate 2025-05-04 00:33:52
* @Entity org.tl.user.provider.entity.MovieWatchHistory
*/
public interface MovieWatchHistoryMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MovieWatchHistory record);

    int insertSelective(MovieWatchHistory record);

    MovieWatchHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MovieWatchHistory record);

    int updateByPrimaryKey(MovieWatchHistory record);

    int deleteMovieWatchHistoryByUserIdAndMovieId(MovieWatchHistory movieWatchHistory);

    List<MovieDO> selectMovieWatchHistoryByUserId(Long userId);
}
