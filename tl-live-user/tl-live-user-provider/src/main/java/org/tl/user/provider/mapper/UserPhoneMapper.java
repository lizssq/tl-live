package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tl.user.provider.entity.UserPhoneDO;

@Repository
@Mapper
public interface UserPhoneMapper extends BaseMapper<UserPhoneDO> {
}
