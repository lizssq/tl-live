package org.tl.user.provider.mapper;

import org.tl.user.provider.entity.UserFollow;

/**
* @author k1341
* @description 针对表【user_follow(用户关注关系表)】的数据库操作Mapper
* @createDate 2025-05-13 15:54:23
* @Entity org.tl.user.provider.entity.UserFollow
*/
public interface UserFollowMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserFollow record);

    int insertSelective(UserFollow record);

    UserFollow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFollow record);

    int updateByPrimaryKey(UserFollow record);

}
