package org.tl.user.provider.mapper;

import org.apache.ibatis.annotations.Param;
import org.tl.user.provider.entity.MovieComment;

import java.util.List;

/**
* @author k1341
* @description 针对表【movie_comment】的数据库操作Mapper
* @createDate 2025-04-21 10:31:55
* @Entity org.tl.user.provider.entity.MovieComment
*/
public interface MovieCommentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MovieComment record);

    int insertSelective(MovieComment record);

    MovieComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MovieComment record);

    int updateByPrimaryKey(MovieComment record);

    /**
     * 根据评论ID联合查询评论信息和用户信息
     * @param movieId 电影ID
     * @return MovieComment对象，包含用户昵称和头像信息
     */
    List<MovieComment> getCommentWithUserInfo(@Param("movie_id") Long movieId);

}
