package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.LiveCategory;

import java.util.List;

/**
* @author k1341
* @description 针对表【live_category(直播分类表)】的数据库操作Mapper
* @createDate 2025-04-22 16:10:45
* @Entity org.tl.user.provider.entity.LiveCategory
*/
public interface LiveCategoryMapper extends BaseMapper<LiveCategory> {

    int deleteByPrimaryKey(Long id);

    int insert(LiveCategory record);

    int insertSelective(LiveCategory record);

    LiveCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LiveCategory record);

    int updateByPrimaryKey(LiveCategory record);

    List<LiveCategory> getCategoryRoomCount();

}
