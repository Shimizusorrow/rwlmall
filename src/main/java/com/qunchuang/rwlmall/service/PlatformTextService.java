package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.PlatformText;

/**
 * @author Curtain
 * @date 2018/4/28 14:48
 */
public interface PlatformTextService {

    PlatformText findByType(Integer type);

    PlatformText save(PlatformText platformText);

    PlatformText update(PlatformText platformText,String platformTextId);
}
