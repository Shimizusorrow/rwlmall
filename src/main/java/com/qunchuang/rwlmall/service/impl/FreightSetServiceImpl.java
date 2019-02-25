package com.qunchuang.rwlmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qunchuang.rwlmall.bean.FreightSet;
import com.qunchuang.rwlmall.enums.FreightCategoryEnum;
import com.qunchuang.rwlmall.service.FreightSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/5/15 10:52
 */

@Service
public class FreightSetServiceImpl implements FreightSetService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void setFreightSet(String key, FreightSet freightSet) {
        String value = JSONObject.toJSONString(freightSet);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public List<FreightSet> getAll() {
        List result = new ArrayList(2);
        result.add(getFreightSet(FreightCategoryEnum.LAUNDRY.getKey()));
        result.add(getFreightSet(FreightCategoryEnum.MALL.getKey()));
        return result;
    }

    @Override
    public FreightSet getFreightSet(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        JSONObject freightSet = (JSONObject) JSON.parse(value);
        return JSONObject.toJavaObject(freightSet, FreightSet.class);
    }

    @Override
    public FreightSet getFreightSet() {
        String value = (String) redisTemplate.opsForValue().get(FreightCategoryEnum.LAUNDRY.getKey());
        JSONObject freightSet = (JSONObject) JSON.parse(value);
        return JSONObject.toJavaObject(freightSet, FreightSet.class);
    }
}
