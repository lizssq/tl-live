package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.tl.user.provider.entity.Messages;

import java.util.List;

/**
* @author k1341
* @description 针对表【messages】的数据库操作Mapper
* @createDate 2025-05-14 20:56:58
* @Entity org.tl.user.provider.entity.Messages
*/
public interface MessagesMapper extends BaseMapper<Messages> {

    int deleteByPrimaryKey(Long id);

    int insert(Messages record);

    int insertSelective(Messages record);

    Messages selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Messages record);

    int updateByPrimaryKey(Messages record);

    @Select("SELECT m.*, u.username as senderName FROM messages m " +
            "JOIN users u ON m.sender_id = u.id " +
            "WHERE m.conversation_id = #{conversationId} " +
            "ORDER BY m.sent_at DESC")
    List<Messages> selectMessagesWithSender(@Param("conversationId") Integer conversationId);

    List<Messages> selectFullHistory(Long conversationId);
}
