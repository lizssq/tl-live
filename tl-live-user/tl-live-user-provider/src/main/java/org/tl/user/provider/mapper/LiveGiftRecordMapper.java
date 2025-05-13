package org.tl.user.provider.mapper;

import org.tl.user.provider.entity.LiveGiftRecord;

/**
* @author k1341
* @description 针对表【live_gift_record(直播打赏核心流水表)】的数据库操作Mapper
* @createDate 2025-05-04 00:33:52
* @Entity org.tl.user.provider.entity.LiveGiftRecord
*/
public interface LiveGiftRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(LiveGiftRecord record);

    int insertSelective(LiveGiftRecord record);

    LiveGiftRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LiveGiftRecord record);

    int updateByPrimaryKey(LiveGiftRecord record);

}
