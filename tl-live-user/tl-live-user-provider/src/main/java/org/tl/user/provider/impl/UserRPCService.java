package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.UserDTO;
import org.tl.user.DTO.UserProfileDTO;
import org.tl.user.inter.IUserRPCService;
import org.tl.user.provider.entity.UserDO;
import org.tl.user.provider.service.UserService;
import org.tl.user.provider.util.UserRedisKeyBuilder;

import java.math.BigDecimal;
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
    public List<UserDTO> getFollowList(Long userId) {
        return userService.getFollowList(userId);
    }

    @Override
    public List<UserDTO> getFollowerList(Long userId) {
        return userService.getFollowerList(userId);
    }

    @Override
    public List<UserDTO> getMutualFollowerList(Long userId) {
        return userService.getMutualFollowerList(userId);
    }
}
