package org.tl.live.Controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.SearchMovieDTO;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.MovieCommentDTO;
import org.tl.user.DTO.MovieDTO;
import org.tl.user.DTO.MovieFavoriteDTO;
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
        if(movieId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE, "movieId为空");
        }
        //redis 操作
        MovieSourceDTO movieSourceDTO = movieRPCService.movieSource(movieId);
        if(movieSourceDTO!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE, movieSourceDTO);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE, "未查询到该电影的数据源");
    }

    @GetMapping("/movieComment")
    public WebResDTO movieComment(Long movieId){
        if(movieId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE, "movieId为空");
        }
        List<MovieCommentDTO> movieCommentDTO = movieRPCService.movieComment(movieId);
        if(movieCommentDTO!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE, movieCommentDTO);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE, "该电影的评论为空");
    }

    @PostMapping("/movieComment")
    public WebResDTO movieComment(@RequestBody MovieCommentDTO comment){
        if(comment!=null&&comment.getMovieId()!=null&&comment.getUserId()!=null&&comment.getContent()!=null){
            //判断用户、电影是否存在，过滤器应检查用户是否登录
            //todo
            int i = movieRPCService.addMovieComment(comment);
            if(i==1){
                return new WebResDTO(WebResDTO.SUCCESS_CODE, "该电影的评论添加成功");
            }
            return new WebResDTO(WebResDTO.ERROR_CODE, "该电影的评论添加失败");
        }
        return new WebResDTO(WebResDTO.ERROR_CODE, "该电影的评论信息不完整");
    }

    @GetMapping("/movieFavorite")
    public WebResDTO getMovieFavorite(Long userId){
        if(userId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"userId为空");
        }
        List<MovieDTO> movieFavoriteByUserId = movieRPCService.getMovieFavoriteByUserId(userId);
        return new WebResDTO(WebResDTO.SUCCESS_CODE,movieFavoriteByUserId);
    }
    @GetMapping("/movieFavorite/{userId}/{movieId}")
    public WebResDTO getMovieFavoriteByUserIdAndMovieId(@PathVariable Long userId,@PathVariable Long movieId){
        if(userId==null||movieId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"userId或movieId为空");
        }
        MovieFavoriteDTO movieFavoriteByUserIdAndMovieId = movieRPCService.getMovieFavoriteByUserIdAndMovieId(userId, movieId);
        if(movieFavoriteByUserIdAndMovieId!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,movieFavoriteByUserIdAndMovieId);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"该电影的收藏信息为空");
    }
    @PutMapping("/movieFavorite")
    public WebResDTO putMovieFavorite(@RequestBody MovieFavoriteDTO movieFavoriteDTO){
        if(movieFavoriteDTO==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"movieFavoriteDTO为空");
        }
        int i = movieRPCService.setMovieFavoriteByUserIdAndMovieId(movieFavoriteDTO);
        return new WebResDTO(WebResDTO.SUCCESS_CODE,i);
    }
    @DeleteMapping("/movieFavorite")
    public WebResDTO deleteMovieFavorite(@RequestBody MovieFavoriteDTO movieFavoriteDTO){
        if(movieFavoriteDTO==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"movieFavoriteDTO为空");
        }
        int i = movieRPCService.deleteMovieFavoriteByUserIdAndMovieId(movieFavoriteDTO);
        return new WebResDTO(WebResDTO.SUCCESS_CODE,i);
    }

    @GetMapping("/movieCategory")
    public WebResDTO getMovieCategoryP(){
        return new WebResDTO(WebResDTO.SUCCESS_CODE,movieRPCService.getCategoryRoomCount());
    }
    @GetMapping("/movieRegion")
    public WebResDTO getMovieRegion(){
        return new WebResDTO(WebResDTO.SUCCESS_CODE,movieRPCService.getRegionMovieCounts());
    }

    @GetMapping("/movieReleaseYear")
    public WebResDTO getMovieReleaseYear(){
        return new WebResDTO(WebResDTO.SUCCESS_CODE,movieRPCService.getReleaseYearMovieCounts());
    }

    @GetMapping("/getSimilarMovies")
    public WebResDTO getSimilarMovies(Long movieId) {
        if(movieId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"movieId为空");
        }
        List<MovieDTO> similarMovies = movieRPCService.getSimilarMovies(movieId);
        if(similarMovies!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,similarMovies);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"该电影的相似电影为空");
    }

}
