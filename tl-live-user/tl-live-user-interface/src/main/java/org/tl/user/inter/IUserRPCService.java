package org.tl.user.inter;

import org.tl.user.DTO.*;

import java.math.BigDecimal;
import java.util.List;

public interface IUserRPCService {
    UserDTO getUserById(Long userId);

    String createToken(Long userId);

    String checkToken(String titk);

    boolean recharge(Long userId, BigDecimal addMoney);

    UserProfileDTO getUserProfile(Long userId);

    boolean follow(Long userId, Long followUserId);

    boolean unfollow(Long userId, Long followUserId);

    PageResult<UserProfileDTO> getFollowList(Long userId, int pageNum, int pageSize);
    PageResult<UserProfileDTO> getFollowerList(Long userId, int pageNum, int pageSize);
    PageResult<UserProfileDTO> getMutualFollowerList(Long userId, int pageNum, int pageSize);

    int saveUserChatMessage(Long userId, Long userId1, String content);

    List<ConversationsDTO> getConversations(Long userId);

    List<MessagesDTO> getHistoryMessages(Long userId, Long conversationId);

    UnreadCountDTO getUnreadCounts(Long userId);

    ConversationsDTO getConversationById(Long conversationId);

    boolean updateUserProfile(UserProfileDTO userProfileDTO);

    int realNameAuthentication(String idCard, String name, Long userId);


    PageResult<UserProfileDTO> search(String keyword, Long userId, Integer pageNum, Integer pageSize);

    UserProfileDTO getUserByRoomId(Long roomId);

    boolean isFollow(Long userId, Long followUserId);
}
