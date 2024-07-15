package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tl.user.provider.entity.SmsDO;

@Repository
@Mapper
public interface SmaMapper extends BaseMapper<SmsDO> {
}
