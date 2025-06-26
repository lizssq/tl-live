package org.tl.user.provider.mapper;

import org.apache.ibatis.annotations.Param;
import org.tl.user.DTO.MovieFavoriteDTO;
import org.tl.user.provider.entity.MovieDO;
import org.tl.user.provider.entity.MovieFavorite;
import org.tl.user.provider.entity.MovieWatchHistory;

import java.util.List;

/**
* @author k1341
* @description 针对表【movie_favorite】的数据库操作Mapper
* @createDate 2025-04-21 17:09:26
* @Entity org.tl.user.provider.entity.MovieFavorite
*/
public interface MovieFavoriteMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MovieFavorite record);

    int insertSelective(MovieFavorite record);

    MovieFavorite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MovieFavorite record);

    int updateByPrimaryKey(MovieFavorite record);

    List<MovieDO> getFavoriteMoviesByUserId(Long userId);

    int deleteFavoriteMovie(MovieFavorite movieFavorite);

    MovieFavorite getMovieFavoriteByUserIdAndMovieId(@Param("userId")Long userId, @Param("movieId")Long movieId);

    int insertHistory(MovieWatchHistory movieWatchHistory);
}
