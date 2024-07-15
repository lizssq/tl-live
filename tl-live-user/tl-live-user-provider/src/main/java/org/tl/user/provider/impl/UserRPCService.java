package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.UserDTO;
import org.tl.user.inter.IUserRPCService;
import org.tl.user.provider.entity.UserDO;
import org.tl.user.provider.service.UserService;
import org.tl.user.provider.util.UserRedisKeyBuilder;

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
        return null;
    }
}
