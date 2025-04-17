package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.MovieDTO;
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
}
