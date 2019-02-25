package com.qunchuang.rwlmall.service;

/**
 * @author Curtain
 * @date 2018/8/22 14:10
 */
public interface RedisService {

    /**
     * 获取缓存的微信token
     * @return
     */
    String getToken();
}
