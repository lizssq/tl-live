package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.RechargePlan;

/**
* @author k1341
* @description 针对表【recharge_plan(充值商品配置表)】的数据库操作Mapper
* @createDate 2025-05-06 21:54:22
* @Entity org.tl.user.provider.entity.RechargePlan
*/
public interface RechargePlanMapper extends BaseMapper<RechargePlan> {

    int deleteByPrimaryKey(Long id);

    int insert(RechargePlan record);

    int insertSelective(RechargePlan record);

    RechargePlan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RechargePlan record);

    int updateByPrimaryKey(RechargePlan record);

    int deleteById(Long id);

}
