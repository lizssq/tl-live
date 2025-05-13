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
    public List<MovieDTO> getMovieFavoriteByUserId(Long userId) {
        return movieService.getMovieFavoriteByUserId(userId);
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

}
