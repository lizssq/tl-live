package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.tl.user.provider.entity.ConversationStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
* @author k1341
* @description 针对表【conversation_status】的数据库操作Mapper
* @createDate 2025-05-14 20:56:58
* @Entity org.tl.user.provider.entity.ConversationStatus
*/
public interface ConversationStatusMapper extends BaseMapper<ConversationStatus> {

    int deleteByPrimaryKey(Long id);

    int insert(ConversationStatus record);

    int insertSelective(ConversationStatus record);

    ConversationStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConversationStatus record);

    int updateByPrimaryKey(ConversationStatus record);

    Integer countByUserAndConversation(@Param("userId")Long userId, @Param("conversationId")Long conversationId);

    void updateUnreadStatus(@Param("userId") Long userId,@Param("conversationId") Long conversationId,@Param("unreadCount") int i,@Param("readTime") Date now);

    List<ConversationStatus> selectByUserId(Long userId);

    void incrementUnreadCount(@Param("conversationId")Long conversationId,@Param("userId") Long userId, @Param("increment") int i);
}
