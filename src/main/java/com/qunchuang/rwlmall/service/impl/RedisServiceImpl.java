package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Curtain
 * @date 2018/8/22 14:13
 */

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String getToken() {
        String token = (String) redisTemplate.opsForValue().get("weChatToken");
        return token;
    }
}
