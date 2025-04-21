package org.tl.user.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.*;
import org.tl.user.provider.entity.MovieComment;
import org.tl.user.provider.entity.MovieDO;
import org.tl.user.provider.entity.MovieFavorite;
import org.tl.user.provider.entity.MovieSource;
import org.tl.user.provider.mapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Resource
    private MovieMapper movieMapper;

    @Resource
    private MovieSourceMapper movieSourceMapper;

    @Resource
    private MoviecategoryrelationMapper movieCategoryRelationMapper;

    @Resource
    private MovieCommentMapper movieCommentMapper;

    @Resource
    private MovieFavoriteMapper movieFavoriteMapper;

    Logger logger = LoggerFactory.getLogger(MovieService.class);

    public List<MovieDTO> getAllMovieInfo(){
        List<MovieDO> movieList= movieMapper.selectList(null);
        List<MovieDTO> dtoList = new ArrayList<>();
        for (MovieDO movie : movieList) {
            MovieDTO dto = new MovieDTO();
            BeanUtils.copyProperties(movie, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        return dtoList;
    }
    public MovieDTO getMovieById(long id){
        //todo
        //从redis中查询

        MovieDO movie= movieMapper.selectById(id);
        //保存到redis数据库

        List<String> categoriesByMovieId = movieCategoryRelationMapper.findCategoriesByMovieId(id);
        movie.setCategory(categoriesByMovieId);


        MovieDTO movieDTO = new MovieDTO();
        BeanUtils.copyProperties(movie, movieDTO);  // 自动拷贝属性

        logger.info(movieDTO.toString());
        return movieDTO;
    }

    public List<MovieDTO> searchMovies(Map<String, Object> searchMovie) {
        LambdaQueryWrapper<MovieDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 获取 keyword 参数
        String keyword = (String) searchMovie.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // 在多个列中进行模糊查询
            lambdaQueryWrapper
                    .or(wrapper -> wrapper.like(MovieDO::getTitle, keyword)) // 假设电影名称字段为 name
                    .or(wrapper -> wrapper.like(MovieDO::getDirector, keyword)) // 假设导演字段为 director
                    .or(wrapper -> wrapper.like(MovieDO::getActors, keyword)) // 假设演员字段为 actor
                    .or(wrapper -> wrapper.like(MovieDO::getDescription, keyword)); // 假设描述字段为 description
        }

        // 其他条件
        if (searchMovie.containsKey("minRating") && searchMovie.get("minRating") != null) {
            lambdaQueryWrapper.ge(MovieDO::getRating, searchMovie.get("minRating")); // 假设电影评分字段为 rating
        }

        if (searchMovie.containsKey("releaseYear") && searchMovie.get("releaseYear") != null) {
            lambdaQueryWrapper.eq(MovieDO::getReleaseYear, searchMovie.get("releaseYear")); // 假设电影上映年份字段为 release_year
        }

        if (searchMovie.containsKey("region") && searchMovie.get("region") != null) {
            lambdaQueryWrapper.eq(MovieDO::getRegion, searchMovie.get("region")); // 假设电影地区字段为 region
        }

        // 执行查询
        List<MovieDO> movies = movieMapper.selectList(lambdaQueryWrapper);
        List<MovieDTO> dtoList = new ArrayList<>();
        for (MovieDO movie : movies) {
            MovieDTO dto = new MovieDTO();
            BeanUtils.copyProperties(movie, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        return dtoList;
    }
    public MovieSourceDTO movieSource(Long movieId){
        MovieSource movieSource = movieSourceMapper.findByMovieIdMovieSource(movieId);
        MovieSourceDTO dto = new MovieSourceDTO();
        BeanUtils.copyProperties(movieSource, dto);
        return dto;
    }
    //评论
    public List<MovieCommentDTO> movieComment (Long movieId) {
        List<MovieComment> movieComment = movieCommentMapper.getCommentWithUserInfo(movieId);
        List<MovieCommentDTO> dtoList=new ArrayList<>();
        for (MovieComment movie : movieComment) {
            MovieCommentDTO dto = new MovieCommentDTO();
            BeanUtils.copyProperties(movie, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        return dtoList;
    }
    public int addMovieComment (MovieCommentDTO comment){
        MovieComment movieComment=new MovieComment();
        BeanUtils.copyProperties(comment,movieComment);
        return movieCommentMapper.insertSelective(movieComment);
    }

    //收藏
    public List<MovieDTO> getMovieFavoriteByUserId(Long userId){
        List<MovieDO> favoriteMovies = movieFavoriteMapper.getFavoriteMoviesByUserId(userId);
        List<MovieDTO> dtoList=new ArrayList<>();
        for (MovieDO movie : favoriteMovies) {
            MovieDTO dto = new MovieDTO();
            BeanUtils.copyProperties(movie, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        return dtoList;
    }
    public int setMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO){
        MovieFavorite movieFavorite=new MovieFavorite();
        BeanUtils.copyProperties(movieFavoriteDTO,movieFavorite);
        return movieFavoriteMapper.insertSelective(movieFavorite);
    }
    public int deleteMovieFavoriteByUserIdAndMovieId(MovieFavoriteDTO movieFavoriteDTO){
        MovieFavorite movieFavorite=new MovieFavorite();
        BeanUtils.copyProperties(movieFavoriteDTO,movieFavorite);
        return movieFavoriteMapper.deleteFavoriteMovie(movieFavorite);
    }

}
