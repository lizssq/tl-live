package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;
import org.tl.user.provider.entity.UserDO;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
