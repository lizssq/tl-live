package org.tl.user.inter;

import org.tl.user.DTO.MovieCommentDTO;
import org.tl.user.DTO.MovieDTO;
import org.tl.user.DTO.MovieFavoriteDTO;
import org.tl.user.DTO.MovieSourceDTO;

import java.util.List;
import java.util.Map;

public interface IMovieRPCService {
    List<MovieDTO> getAllMovieInfo();
    
    MovieDTO getMovieById(long id);

    List<MovieDTO> searchMovies(Map<String,Object> searchMovie);

    MovieSourceDTO movieSource(Long id);

    List<MovieCommentDTO> movieComment(Long movieId);

    int addMovieComment(MovieCommentDTO comment);

    List<MovieDTO> getMovieFavoriteByUserId(Long userId);

    int setMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO);
    int deleteMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO);

}
