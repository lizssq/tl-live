package org.tl.user.inter;

import org.tl.user.DTO.*;

import java.util.List;
import java.util.Map;

public interface IMovieRPCService {
    List<MovieDTO> getAllMovieInfo();
    
    MovieDTO getMovieById(long id);

    List<MovieDTO> searchMovies(Map<String,Object> searchMovie);

    MovieSourceDTO movieSource(Long id);

    List<MovieCommentDTO> movieComment(Long movieId);

    int addMovieComment(MovieCommentDTO comment);

    PageResult<MovieDTO> getMovieFavoriteByUserId(Long userId, int pageNum, int pageSize);

    int setMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO);
    int deleteMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO);

    List<RegionMovieCount> getCategoryRoomCount();

    List<RegionMovieCount> getRegionMovieCounts();

    List<RegionMovieCount> getReleaseYearMovieCounts();

    List<MovieDTO> getSimilarMovies(Long movieId);

    MovieFavoriteDTO getMovieFavoriteByUserIdAndMovieId(Long userId, Long movieId);

    int addHistory(MovieWatchHistoryDTO movieWatchHistoryDTO);

    PageResult<MovieDTO> getMovieWatchHistoryByUserId(Long userId, Integer pageNum, Integer pageSize);

    int deleteMovieWatchHistoryByUserIdAndMovieId(MovieWatchHistoryDTO movieWatchHistoryDTO);

    PageResult<MovieDTO> search(String keyword, Integer pageNum, Integer pageSize);

    List<MovieDTO> getTopRatedMovies();
}
