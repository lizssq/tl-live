package org.tl.user.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.*;
import org.tl.user.provider.entity.*;
import org.tl.user.provider.mapper.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Resource
    private MovieWatchHistoryMapper movieWatchHistoryMapper;

    @Resource
    private MoviecategoryMapper moviecategoryMapper;

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

        MovieDO movie= movieMapper.selectByPrimaryKey(id);
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
    public PageResult<MovieDTO> getMovieFavoriteByUserId(Long userId, int pageNum, int pageSize) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<MovieDO> favoriteMovies = movieFavoriteMapper.getFavoriteMoviesByUserId(userId);
        Page<MovieDO> pageInfo = (Page<MovieDO>) favoriteMovies;
        // 转换DTO列表
        List<MovieDTO> dtos = favoriteMovies.stream()
                .map(favoriteMovie -> {
                    MovieDTO dto = new MovieDTO();
                    BeanUtils.copyProperties(favoriteMovie, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建分页结果
        PageResult<MovieDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
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

    public List<RegionMovieCount>  getTopLevelCategoryCounts(){
        return moviecategoryMapper.getTopLevelCategoryCounts();
    }

    public List<RegionMovieCount> getRegionMovieCounts() {
        List<Map<String, Object>> results = movieMapper.getRegionMovieCounts();

        // 为每个地区分配一个自增的 id
        return IntStream.range(1, results.size() + 1)
                .mapToObj(id -> {
                    Map<String, Object> result = results.get(id - 1);
                    return new RegionMovieCount(
                            id,
                            (String) result.get("name"),
                            ((Number) result.get("count")).intValue()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<RegionMovieCount> getReleaseYearMovieCounts() {
        List<RegionMovieCount> releaseYearMovieCounts = movieMapper.getReleaseYearMovieCounts();
        // 为每个年份分配一个自增的 id
        for (int i = 0; i < releaseYearMovieCounts.size(); i++) {
            releaseYearMovieCounts.get(i).setId(i + 1);
        }
        return movieMapper.getReleaseYearMovieCounts();
    }

    //获取同类电影
    public List<MovieDTO> getSimilarMovies(Long movieId) {
        // 1. 获取所有相关分类ID
        PageHelper.startPage(1, 10);
        List<MovieDO> moviesByCategoryIds = movieMapper.selectSimilarMoviesPage(movieId);
        List<MovieDTO> dtoList=new ArrayList<>();
        for (MovieDO movie : moviesByCategoryIds) {
            MovieDTO dto = new MovieDTO();
            BeanUtils.copyProperties(movie, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        // 2. 查询同类电影
        return dtoList;
    }

    public MovieFavoriteDTO getMovieFavoriteByUserIdAndMovieId(Long userId, Long movieId) {
        MovieFavorite movieFavoriteByUserIdAndMovieId = movieFavoriteMapper.getMovieFavoriteByUserIdAndMovieId(userId, movieId);
        if (movieFavoriteByUserIdAndMovieId != null) {
            MovieFavoriteDTO movieFavoriteDTO = new MovieFavoriteDTO();
            BeanUtils.copyProperties(movieFavoriteByUserIdAndMovieId, movieFavoriteDTO);
            return movieFavoriteDTO;
        }
        return null;
    }

    public int addHistory(MovieWatchHistoryDTO movieWatchHistoryDTO) {
        MovieWatchHistory movieWatchHistory = new MovieWatchHistory();
        BeanUtils.copyProperties(movieWatchHistoryDTO, movieWatchHistory);
        return movieFavoriteMapper.insertHistory(movieWatchHistory);
    }

    public int deleteMovieWatchHistoryByUserIdAndMovieId(MovieWatchHistoryDTO movieWatchHistoryDTO) {
        MovieWatchHistory movieWatchHistory = new MovieWatchHistory();
        BeanUtils.copyProperties(movieWatchHistoryDTO, movieWatchHistory);
        return movieWatchHistoryMapper.deleteMovieWatchHistoryByUserIdAndMovieId(movieWatchHistory);
    }

    public PageResult<MovieDTO> getMovieWatchHistoryByUserId(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MovieDO> movieDOList = movieWatchHistoryMapper.selectMovieWatchHistoryByUserId(userId);
        Page<MovieDO> pageInfo = (Page<MovieDO>) movieDOList;
        // 转换DTO列表
        List<MovieDTO> dtos = movieDOList.stream()
                .map(movie -> {
                    MovieDTO dto = new MovieDTO();
                    BeanUtils.copyProperties(movie, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        // 构建分页结果
        PageResult<MovieDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    public PageResult<MovieDTO> search(String keyword, Integer pageNum, Integer pageSize) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<MovieDO> movies = movieMapper.searchMovies(keyword);
        Page<MovieDO> pageInfo = (Page<MovieDO>) movies;
        // 转换DTO列表
        List<MovieDTO> dtos = movies.stream()
                .map(movie -> {
                    MovieDTO dto = new MovieDTO();
                    BeanUtils.copyProperties(movie, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        // 构建分页结果
        PageResult<MovieDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    public List<MovieDTO> getTopRatedMovies() {
        // 1. 获取所有相关分类ID
        List<MovieDO> moviesByCategoryIds = movieMapper.getTopRatedMovies();
        List<MovieDTO> dtoList=new ArrayList<>();
        for (MovieDO movie : moviesByCategoryIds) {
            MovieDTO dto = new MovieDTO();
            BeanUtils.copyProperties(movie, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        return dtoList;
    }
}
