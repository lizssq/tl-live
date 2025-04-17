package org.tl.user.inter;

import org.tl.user.DTO.MovieDTO;

import java.util.List;
import java.util.Map;

public interface IMovieRPCService {
    List<MovieDTO> getAllMovieInfo();
    
    MovieDTO getMovieById(long id);

    List<MovieDTO> searchMovies(Map<String,Object> searchMovie);
}
