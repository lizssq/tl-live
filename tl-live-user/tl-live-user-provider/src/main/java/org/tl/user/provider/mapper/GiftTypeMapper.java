package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.GiftType;

/**
* @author k1341
* @description 针对表【gift_type(礼物配置表)】的数据库操作Mapper
* @createDate 2025-05-04 00:33:52
* @Entity org.tl.user.provider.entity.GiftType
*/
public interface GiftTypeMapper extends BaseMapper<GiftType> {

    int deleteByPrimaryKey(Long id);

    int insert(GiftType record);

    int insertSelective(GiftType record);

    GiftType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GiftType record);

    int updateByPrimaryKey(GiftType record);

    GiftType selectByGiftName(String giftName);

    //逻辑删除
    int deleteById(Long id);

    // name查找
    GiftType selectByName(String name);

}
