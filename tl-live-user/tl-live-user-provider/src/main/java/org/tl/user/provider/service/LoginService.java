package org.tl.user.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tl.common.redis.builder.UserCacheKeyBuilder;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.LoginDTO;
import org.tl.user.DTO.UserDTO;
import org.tl.user.DTO.UserPhoneDTO;
import org.tl.user.provider.entity.UserPhoneDO;
import org.tl.user.provider.mapper.UserPhoneMapper;
import org.tl.user.provider.util.MobileRedisKeyBuilder;

import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    @Resource
    private UserCacheKeyBuilder userCacheKeyBuilder;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserPhoneMapper userPhoneMapper;
    private ConvertBeanUtil convertBeanUtil=new ConvertBeanUtil();
    @Resource
    UserService userService;
    public LoginDTO loginByPhone(String phone) {
        //校验数据
        if(phone == null || phone.length() != 11) {
            return new LoginDTO(false, "手机号格式错误");
        }
        //查看是否注册过
        LoginDTO loginDTO=this.queryByPhone(phone);
        if(loginDTO!=null){
            return loginDTO;
        }
        return null;
    }

    private LoginDTO queryByPhone(String phone) {
        //检查redis中数据
        //生成key
        String key = userCacheKeyBuilder.getUserPhoneKey(phone);
        //查询redis
        if(redisTemplate.hasKey(key)) {
            return (LoginDTO)redisTemplate.opsForValue().get(key);
        }
        LoginDTO loginDTO = this.selectByPhone(phone);
        //查询数据库
        if(loginDTO!= null) {
            //写入redis
            redisTemplate.opsForValue().set(key, loginDTO,30, TimeUnit.MINUTES);
            return loginDTO;
        }
        //缓存击穿
        UserPhoneDTO newUserPhoneDTO = new UserPhoneDTO();
        newUserPhoneDTO.setId(-1L);
        redisTemplate.opsForValue().set(key, newUserPhoneDTO, 5, TimeUnit.SECONDS);

        //注册登录
        return registerAndLogin(phone);
    }
    public LoginDTO selectByPhone(String phone) {
        LambdaQueryWrapper<UserPhoneDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhoneDO::getPhone, phone);
        queryWrapper.eq(UserPhoneDO::getStatus, commonStatusEnum.VALID_USER.getCode());
        queryWrapper.last("limit 1");
        UserPhoneDO userPhoneDO = userPhoneMapper.selectOne(queryWrapper);
        if(userPhoneDO != null) {
            return LoginDTO.success(userPhoneDO.getUserId());
        }
        return null;
    }

    public LoginDTO registerAndLogin(String phone) {
        //注册
        LoginDTO loginDTO = userService.registerByPhone(phone);
        //删除redis
        String key = userCacheKeyBuilder.getUserPhoneKey(phone);
        redisTemplate.delete(key);
        return loginDTO;
    }
}
