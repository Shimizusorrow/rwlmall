package com.qunchuang.rwlmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 10:49
 */

@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void setRecharge(String key, Recharge recharge) {
        String value = JSONObject.toJSONString(recharge);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Recharge getRecharge(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        JSONObject recharge = (JSONObject) JSON.parse(value);
        return JSONObject.toJavaObject(recharge, Recharge.class);
    }


    @Override
    public List<Recharge> getAll() {
        List<Recharge> recharges = new ArrayList<>();
        for (Integer i : Arrays.asList(1, 2, 3, 4)) {
            String value = (String) redisTemplate.opsForValue().get("rwlRecharge" + i);
            JSONObject recharge = (JSONObject) JSON.parse(value);
            recharges.add(JSONObject.toJavaObject(recharge, Recharge.class));
        }
        return recharges;
    }
}
