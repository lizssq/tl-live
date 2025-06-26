package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.tl.user.provider.entity.Conversations;

import java.util.List;

/**
* @author k1341
* @description 针对表【conversations】的数据库操作Mapper
* @createDate 2025-05-14 20:56:58
* @Entity org.tl.user.provider.entity.Conversations
*/
public interface ConversationsMapper extends BaseMapper<Conversations> {

    int deleteByPrimaryKey(Long id);

    int insert(Conversations record);

    int insertSelective(Conversations record);

    Conversations selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Conversations record);

    int updateByPrimaryKey(Conversations record);

    @Select("SELECT * FROM conversations WHERE conversation_type = 0 " +
            "AND ((user1_id = #{user1} AND user2_id = #{user2}) " +
            "OR (user1_id = #{user2} AND user2_id = #{user1}))")
    Conversations findPrivateConversation(@Param("user1") Integer user1,
                                         @Param("user2") Integer user2);

    List<Conversations> selectByUserId(int intExact);

    Conversations selectByUserIdAndType(@Param("user1Id") int user1Id,@Param("conversationType") Integer conversationType);
}
