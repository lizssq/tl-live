package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.*;
import org.tl.user.inter.IMovieRPCService;
import org.tl.user.provider.service.MovieService;

import java.util.List;
import java.util.Map;

@DubboService
public class MovieRPCService implements IMovieRPCService {
    @Resource
    private MovieService movieService;

    @Override
    public List<MovieDTO> getAllMovieInfo() {
        return movieService.getAllMovieInfo();
    }

    @Override
    public MovieDTO getMovieById(long id) {
        return movieService.getMovieById(id);
    }

    @Override
    public List<MovieDTO> searchMovies(Map<String,Object> searchMovie) {
        return movieService.searchMovies(searchMovie);
    }

    @Override
    public MovieSourceDTO movieSource(Long id) {
        return movieService.movieSource(id);
    }

    @Override
    public List<MovieCommentDTO> movieComment(Long movieId) {
        return movieService.movieComment(movieId);
    }

    @Override
    public int addMovieComment(MovieCommentDTO comment) {
        return movieService.addMovieComment(comment);
    }

    @Override
    public PageResult<MovieDTO> getMovieFavoriteByUserId(Long userId, int pageNum, int pageSize) {
        return movieService.getMovieFavoriteByUserId(userId, pageNum, pageSize);
    }

    @Override
    public int setMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO) {
        return movieService.setMovieFavoriteByUserIdAndMovieId(movieFavoriteDTO);
    }

    @Override
    public int deleteMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO) {
        return movieService.deleteMovieFavoriteByUserIdAndMovieId(movieFavoriteDTO);
    }

    @Override
    public List<RegionMovieCount> getCategoryRoomCount() {
        return movieService.getTopLevelCategoryCounts();
    }

    @Override
    public List<RegionMovieCount> getRegionMovieCounts() {
        return movieService.getRegionMovieCounts();
    }

    @Override
    public List<RegionMovieCount> getReleaseYearMovieCounts() {
        return movieService.getReleaseYearMovieCounts();
    }

    @Override
    public List<MovieDTO> getSimilarMovies(Long movieId) {
        return movieService.getSimilarMovies(movieId);
    }

    @Override
    public MovieFavoriteDTO getMovieFavoriteByUserIdAndMovieId(Long userId, Long movieId) {
        return movieService.getMovieFavoriteByUserIdAndMovieId(userId, movieId);
    }

    @Override
    public int addHistory(MovieWatchHistoryDTO movieWatchHistoryDTO) {
        return movieService.addHistory(movieWatchHistoryDTO);
    }

    @Override
    public PageResult<MovieDTO> getMovieWatchHistoryByUserId(Long userId, Integer pageNum, Integer pageSize) {
        return movieService.getMovieWatchHistoryByUserId(userId, pageNum, pageSize);
    }

    @Override
    public int deleteMovieWatchHistoryByUserIdAndMovieId(MovieWatchHistoryDTO movieWatchHistoryDTO) {
        return movieService.deleteMovieWatchHistoryByUserIdAndMovieId(movieWatchHistoryDTO);
    }

    @Override
    public PageResult<MovieDTO> search(String keyword, Integer pageNum, Integer pageSize) {
        return movieService.search(keyword, pageNum, pageSize);
    }

    @Override
    public List<MovieDTO> getTopRatedMovies() {
        return movieService.getTopRatedMovies();
    }

}
