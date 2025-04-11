package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.tl.common.redis.builder.UserCacheKeyBuilder;
import org.tl.live.commonStatusEunm.commonStatusEnum;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.LoginDTO;
import org.tl.user.DTO.UserDTO;
import org.tl.user.DTO.UserPhoneDTO;
import org.tl.user.provider.entity.UserDO;
import org.tl.user.provider.entity.UserPhoneDO;
import org.tl.user.provider.mapper.UserMapper;
import org.tl.user.provider.mapper.UserPhoneMapper;
import org.tl.user.provider.util.UserRedisKeyBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    UserRedisKeyBuilder userRedisKeyBuilder;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    UserPhoneMapper userPhoneMapper;
    @Resource
    UserCacheKeyBuilder userCacheKeyBuilder;
    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;

    ConvertBeanUtil convertBeanUtil=new ConvertBeanUtil();

    public UserDTO getUserById(Long userId){
        if(userId==null){
            return null;
        }
        String userRedisKey = userRedisKeyBuilder.getUserInfoKey(userId);
        Object userObj = redisTemplate.opsForValue().get(userRedisKey);

        if (userObj == null) {
            // 处理缓存未命中的情况
            // 可能需要从数据库加载数据并存储到Redis
            UserDO userDO = userMapper.selectById(userId);
            if(userDO!=null){
                UserDTO userDTO = convertBeanUtil.convert(userDO, UserDTO.class);
                redisTemplate.opsForValue().set(userRedisKeyBuilder.getUserInfoKey(userId),userDTO,30, TimeUnit.MINUTES);
                return userDTO;
            }else{
                UserDTO noUserDTO=new UserDTO();
                noUserDTO.setUserId(-1L);
                redisTemplate.opsForValue().set(userRedisKeyBuilder.getUserInfoKey(userId),noUserDTO,30, TimeUnit.MINUTES);
                return null;
            }
        } else {
            UserDTO userDTO = (UserDTO) userObj;
            // 使用userDTO进行后续操作
            //userDTO= (UserDTO) redisTemplate.opsForValue().get(userRedisKeyBuilder.getUserInfoKey(userId));
            if(userDTO!=null&&userDTO.getUserId()>0){
                return userDTO;
            }else if(userDTO!=null && userDTO.getUserId()<0){
                return null;
            }
        }
        return null;
    }

    public LoginDTO  registerByPhone(String phone) {
        UserDO userDO = new UserDO();
        //TODO 分配主键

        Long userId=generateIDRPCService.getSequentialID();
        userDO.setUserId(userId);
        userDO.setNickName("用户_"+userId);
        userDO.setAvatar("/img/avatar.png");
        userMapper.insert(userDO);

        UserPhoneDO userPhoneDO = new UserPhoneDO();
        userPhoneDO.setPhone(phone);
        userPhoneDO.setUserId(userId);
        userPhoneDO.setStatus(commonStatusEnum.VALID_USER.getCode());
        userPhoneMapper.insert(userPhoneDO);

        return LoginDTO.success(userId);
    }

    public String createToken(Long userId) {

        //生成token，随机数
        String token = UUID.randomUUID().toString();

        String userCacheKey = userCacheKeyBuilder.getUserPhoneKey(token);
        //存入redis
        redisTemplate.opsForValue().set(userCacheKey, userId,30, TimeUnit.MINUTES);

        return token;
    }

    public String checkToken(String titk) {
        String userCacheKey = userCacheKeyBuilder.getUserPhoneKey(titk);
        Object userId = redisTemplate.opsForValue().get(userCacheKey);
        if(userId!=null){
            return userId.toString();
        }
        return null;
    }
}

