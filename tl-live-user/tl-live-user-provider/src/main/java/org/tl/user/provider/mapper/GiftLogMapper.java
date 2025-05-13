package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.GiftLog;

import java.util.List;

/**
* @author k1341
* @description 针对表【gift_log】的数据库操作Mapper
* @createDate 2025-05-04 21:54:17
* @Entity org.tl.user.provider.entity.GiftLog
*/
public interface GiftLogMapper extends BaseMapper<GiftLog> {

    int deleteByPrimaryKey(Long id);

    int insert(GiftLog record);

    int insertSelective(GiftLog record);

    GiftLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GiftLog record);

    int updateByPrimaryKey(GiftLog record);

    List<GiftLog> selectBySenderId(Long senderId);

    List<GiftLog> selectByReceiverId(Long receiverId);

    List<GiftLog> selectAll(Long roomId);

}
