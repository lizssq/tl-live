package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.*;
import org.tl.user.inter.IUserRPCService;
import org.tl.user.provider.entity.UserDO;
import org.tl.user.provider.service.UserService;
import org.tl.user.provider.util.UserRedisKeyBuilder;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

@DubboService
public class UserRPCService implements IUserRPCService {
    @Resource
    private UserService userService;

    @Override
    public UserDTO getUserById(Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return userDTO;
    }
    @Override
    public String createToken(Long userId) {
        return userService.createToken(userId);
    }

    @Override
    public String checkToken(String titk) {
        return userService.checkToken(titk);
    }

    @Override
    public boolean recharge(Long userId, BigDecimal addMoney) {
        return userService.recharge(userId, addMoney);
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        return userService.getUserProfile(userId);
    }

    @Override
    public boolean follow(Long userId, Long followUserId) {
        return userService.follow(userId, followUserId);
    }

    @Override
    public boolean unfollow(Long userId, Long followUserId) {
        return userService.unfollow(userId, followUserId);
    }

    @Override
    public PageResult<UserProfileDTO> getFollowList(Long userId , int pageNum, int pageSize) {
        return userService.getFollowList(userId, pageNum, pageSize);
    }

    @Override
    public PageResult<UserProfileDTO> getFollowerList(Long userId, int pageNum, int pageSize) {
        return userService.getFollowerList(userId, pageNum, pageSize);
    }

    @Override
    public PageResult<UserProfileDTO> getMutualFollowerList(Long userId, int pageNum, int pageSize) {
        return userService.getMutualFollowerList(userId, pageNum, pageSize);
    }

    @Override
    public int saveUserChatMessage(Long userId, Long userId1, String content) {
        return userService.saveUserChatMessage(userId, userId1, content);
    }

    @Override
    public List<ConversationsDTO> getConversations(Long userId) {
        return userService.getConversations(userId);
    }

    @Override
    public List<MessagesDTO> getHistoryMessages(Long userId, Long conversationId) {
        try {
            return userService.getHistoryMessages(userId, conversationId);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UnreadCountDTO getUnreadCounts(Long userId) {
        return userService.getUnreadCounts(userId);
    }

    @Override
    public ConversationsDTO getConversationById(Long conversationId) {
        return userService.getConversationById(conversationId);
    }

    @Override
    public boolean updateUserProfile(UserProfileDTO userProfileDTO) {
        return userService.updateUserProfile(userProfileDTO);
    }

    @Override
    public int realNameAuthentication(String idCard, String name, Long userId) {
        return userService.realNameAuthentication(idCard, name, userId);
    }

    @Override
    public PageResult<UserProfileDTO> search(String keyword, Long userId, Integer pageNum, Integer pageSize) {
        return userService.search(keyword, userId, pageNum, pageSize);
    }

    @Override
    public UserProfileDTO getUserByRoomId(Long roomId) {
        return userService.getUserByRoomId(roomId);
    }

    @Override
    public boolean isFollow(Long userId, Long followUserId) {
        return userService.isFollow(userId, followUserId);
    }
}
