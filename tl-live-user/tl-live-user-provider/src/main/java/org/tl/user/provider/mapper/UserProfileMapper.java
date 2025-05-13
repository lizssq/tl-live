package org.tl.user.provider.mapper;

import org.tl.user.provider.entity.UserProfile;

/**
* @author k1341
* @description 针对表【user_profile】的数据库操作Mapper
* @createDate 2025-05-05 17:05:06
* @Entity org.tl.user.provider.entity.UserProfile
*/
public interface UserProfileMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserProfile record);

    int insertSelective(UserProfile record);

    UserProfile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserProfile record);

    int updateByPrimaryKey(UserProfile record);


    UserProfile selectByUserId(Long userId);
}
