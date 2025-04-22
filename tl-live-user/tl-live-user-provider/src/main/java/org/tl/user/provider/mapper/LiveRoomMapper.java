package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.LiveRoom;

/**
* @author k1341
* @description 针对表【live_room】的数据库操作Mapper
* @createDate 2025-04-21 20:54:10
* @Entity org.tl.user.provider.entity.LiveRoom
*/
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {

    int deleteByPrimaryKey(Long id);

    int insert(LiveRoom record);

    int insertSelective(LiveRoom record);

    LiveRoom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LiveRoom record);

    int updateByPrimaryKey(LiveRoom record);

    LiveRoom selectByUserId(Long userId);

}
