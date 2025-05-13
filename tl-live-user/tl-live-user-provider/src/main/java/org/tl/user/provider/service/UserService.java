package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tl.common.redis.builder.UserCacheKeyBuilder;
import org.tl.live.commonStatusEunm.commonStatusEnum;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.LoginDTO;
import org.tl.user.DTO.UserDTO;
import org.tl.user.DTO.UserPhoneDTO;
import org.tl.user.DTO.UserProfileDTO;
import org.tl.user.provider.entity.UserDO;
import org.tl.user.provider.entity.UserPhoneDO;
import org.tl.user.provider.entity.UserProfile;
import org.tl.user.provider.mapper.UserMapper;
import org.tl.user.provider.mapper.UserPhoneMapper;
import org.tl.user.provider.mapper.UserProfileMapper;
import org.tl.user.provider.util.UserRedisKeyBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
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
    @Resource
    private
    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;

    @Resource
    private UserProfileMapper userProfileMapper;

    Logger logger = LoggerFactory.getLogger(UserService.class);

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

    //转账
    @Transactional
    public boolean transfer(Long fromId,Long toId, BigDecimal totalCost) {
        // 1. 获取用户信息
        UserProfile fromUserProfile = userProfileMapper.selectByPrimaryKey(fromId);
        UserProfile toUserProfile = userProfileMapper.selectByPrimaryKey(toId);
        if (fromUserProfile == null || toUserProfile == null) {
            return false; // 用户不存在
        }

        // 2. 检查余额是否足够
        if (fromUserProfile.getBalance().compareTo(totalCost) < 0) {
            return false; // 余额不足
        }

        // 3. 扣除余额
        fromUserProfile.setBalance(fromUserProfile.getBalance().subtract(totalCost));
        userProfileMapper.updateByPrimaryKeySelective(fromUserProfile);
        // 4. 增加余额
        toUserProfile.setBalance(toUserProfile.getBalance().add(totalCost));
        userProfileMapper.updateByPrimaryKeySelective(toUserProfile);

        return true;
    }

    //充值
    @Transactional
    public boolean recharge(Long userId, BigDecimal addMoney) {
        // 1. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            logger.info("用户不存在");
            return false; // 用户不存在
        }

        // 2. 增加余额
        userProfile.setBalance(userProfile.getBalance().add(addMoney));
        userProfileMapper.updateByPrimaryKeySelective(userProfile);

        return true;
    }
    //获取用户个人信息
    public UserProfileDTO getUserProfile(Long userId) {
        // 1. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return null; // 用户不存在
        }

        // 2. 转换为 DTO
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userProfile, userProfileDTO);  // 自动拷贝属性

        return userProfileDTO;
    }

    //扣除余额
    @Transactional
    public boolean deductBalance(Long userId, BigDecimal amount) {
        // 1. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return false; // 用户不存在
        }

        // 2. 检查余额是否足够
        if (userProfile.getBalance().compareTo(amount) < 0) {
            return false; // 余额不足
        }

        // 3. 扣除余额
        userProfile.setBalance(userProfile.getBalance().subtract(amount));
        userProfileMapper.updateByPrimaryKeySelective(userProfile);

        return true;
    }

    public boolean follow(Long userId, Long followUserId) {
        return false;
    }

    public boolean unfollow(Long userId, Long followUserId) {
        return false;
    }

    public List<UserDTO> getFollowList(Long userId) {
        return null;
    }

    public List<UserDTO> getFollowerList(Long userId) {
        return null;
    }

    public List<UserDTO> getMutualFollowerList(Long userId) {
        return null;
    }
}

