package org.tl.live.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tl.common.redis.builder.IMCacheKeyBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
@Service
public class IMTokenService {
    @Resource
    private IMCacheKeyBuilder imCacheKeyBuilder;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public String generateIMToken(String userId) {
        //生产随机字符串
        String token = generateRandomString(8);
        //生成key
        String imKey = imCacheKeyBuilder.getIMTokenKey(userId);
        //存储到redis中
        redisTemplate.opsForValue().set(imKey,token,60, TimeUnit.MINUTES);
        return token;
    }

    public boolean checkIMToken(String userId,String token){
        //从redis中获取token
        String imKey = imCacheKeyBuilder.getIMTokenKey(userId);
        String redisToken = (String) redisTemplate.opsForValue().get(imKey);
        //比较
        if(token.equals(redisToken)){
            return true;
        }
        return false;
    }

    private String generateRandomString(int length) {
        String result = "";
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < length; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, str.length());
            result += str.charAt(random);
        }
        return result;
    }
}
