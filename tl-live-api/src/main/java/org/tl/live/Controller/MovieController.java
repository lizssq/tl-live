package org.tl.live.Controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.SearchMovieDTO;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.MovieDTO;
import org.tl.user.DTO.MovieSourceDTO;
import org.tl.user.inter.IMovieRPCService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @DubboReference(check = false)
    private IMovieRPCService movieRPCService;

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/getAllMovies")
    public WebResDTO getAllMovies(){
        List<MovieDTO> res = movieRPCService.getAllMovieInfo();
        logger.info(res.toString());
        if(res!=null && !res.isEmpty()){
            logger.info(res.toString());
            return new WebResDTO(WebResDTO.SUCCESS_CODE,res);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,res);
    }

    @GetMapping("/getMovieById")
    public WebResDTO getMovieById(Long movieId){
        MovieDTO res = movieRPCService.getMovieById(movieId);
        if(res!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,res);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"id错误");
    }

    @PostMapping("/searchMovies")
    public WebResDTO searchMovies(@RequestBody SearchMovieDTO searchMovieDTO) {
        Map<String,Object> searchMovie= new HashMap<>();
        searchMovie.put("keyword",searchMovieDTO.getKeyword());
        searchMovie.put("region",searchMovieDTO.getRegion());
        searchMovie.put("minRating",searchMovieDTO.getMinRating());
        searchMovie.put("releaseYear",searchMovieDTO.getReleaseYear());
        List<MovieDTO> movies = movieRPCService.searchMovies(searchMovie);
        return new WebResDTO(WebResDTO.SUCCESS_CODE, movies);
    }
    @GetMapping("/movieSource")
    public WebResDTO movieSource(Long movieId ){
        //redis 操作
        MovieSourceDTO movieSourceDTO = movieRPCService.movieSource(movieId);
        if(movieSourceDTO!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE, movieSourceDTO);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE, "未查询到该电影的数据源");
    }
}
