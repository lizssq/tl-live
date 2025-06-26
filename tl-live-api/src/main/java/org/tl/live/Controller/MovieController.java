package org.tl.live.Controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.SearchMovieDTO;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.*;
import org.tl.user.inter.IMovieRPCService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.tl.live.enlity.WebResDTO.ERROR_CODE;
import static org.tl.live.enlity.WebResDTO.SUCCESS_CODE;

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
    public WebResDTO getMovieFavorite(Long userId,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        if(userId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"userId为空");
        }
        PageResult<MovieDTO> movieFavoriteByUserId = movieRPCService.getMovieFavoriteByUserId(userId, pageNum, pageSize);
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

    @PostMapping("/history")
    public WebResDTO addHistory(@RequestBody MovieWatchHistoryDTO movieWatchHistoryDTO){
        if(movieWatchHistoryDTO==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"movieHistoryDTO为空");
        }
        //todo
        //判断用户、电影是否存在，过滤器应检查用户是否登录
        if(movieWatchHistoryDTO.getUserId()==null||movieWatchHistoryDTO.getMovieId()==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"userId或movieId为空");
        }
        movieRPCService.addHistory(movieWatchHistoryDTO);
        return new WebResDTO(WebResDTO.SUCCESS_CODE,"添加成功");
    }

    @GetMapping("/history")
    public WebResDTO getHistory(@RequestParam Long userId,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize){
        if(userId==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"userId为空");
        }
        PageResult<MovieDTO> movieWatchHistoryByUserId = movieRPCService.getMovieWatchHistoryByUserId(userId, pageNum, pageSize);
        return new WebResDTO(WebResDTO.SUCCESS_CODE,movieWatchHistoryByUserId);
    }

    @DeleteMapping("/history")
    public WebResDTO deleteHistory(@RequestBody MovieWatchHistoryDTO movieWatchHistoryDTO){
        if(movieWatchHistoryDTO==null){
            return new WebResDTO(WebResDTO.ERROR_CODE,"movieWatchHistoryDTO为空");
        }
        int i = movieRPCService.deleteMovieWatchHistoryByUserIdAndMovieId(movieWatchHistoryDTO);
        return new WebResDTO(WebResDTO.SUCCESS_CODE,"删除成功,删除条数："+i);
    }
    //动态模糊查询
    @GetMapping("/search")
    public WebResDTO search(@RequestParam("keyword") String keyword,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            return new WebResDTO(ERROR_CODE, "搜索关键字不能为空");
        }
        PageResult<MovieDTO> searchResults = movieRPCService.search(keyword, pageNum, pageSize);
        if (searchResults != null) {
            return new WebResDTO(SUCCESS_CODE, searchResults);
        } else {
            return new WebResDTO(ERROR_CODE, "没有搜索结果");
        }
    }
    //获取最高评分的六部电影
    @GetMapping("/getTopRatedMovies")
    public WebResDTO getTopRatedMovies() {
        List<MovieDTO> topRatedMovies = movieRPCService.getTopRatedMovies();
        if (topRatedMovies != null && !topRatedMovies.isEmpty()) {
            return new WebResDTO(SUCCESS_CODE, topRatedMovies);
        } else {
            return new WebResDTO(ERROR_CODE, "没有找到评分最高的电影");
        }
    }

}
