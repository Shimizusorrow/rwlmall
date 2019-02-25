package com.qunchuang.rwlmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Json;
import com.qunchuang.rwlmall.bean.Commission;
import com.qunchuang.rwlmall.bean.FreightSet;
import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.enums.FreightCategoryEnum;
import com.qunchuang.rwlmall.service.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 14:52
 */

@Service
public class CommissionServiceImpl implements CommissionService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void setCommission(String key, Commission commission) {
        String value = JSON.toJSONString(commission);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void initRedisData() {
        //初始化提成
        for (String key : Arrays.asList("Laundry", "HighLaundry", "Furniture", "Mall")) {
            key = "commission" + key;
            Commission commission = new Commission();
            commission.setKey(key);
            commission.setPlatform(20);
            commission.setExpress(0);
            commission.setAgent(80);
            String value = JSON.toJSONString(commission);
            redisTemplate.opsForValue().set(key, value);
        }
        //初始化充值奖励
        for (Integer i : Arrays.asList(1, 2, 3, 4)) {
            String key = "rwlRecharge" + i;
            Recharge recharge = new Recharge();
            recharge.setKey(key);
            recharge.setPayMoney(5000);
            recharge.setRewardMoney(100);

            String value = JSON.toJSONString(recharge);
            redisTemplate.opsForValue().set(key,value);
        }
        //初始化运费设置
        FreightSet freightSet = new FreightSet();
        freightSet.setKey(FreightCategoryEnum.LAUNDRY.getKey());
        freightSet.setFreight(5000L);
        freightSet.setFreight(2000L);
        String value = JSON.toJSONString(freightSet);
        redisTemplate.opsForValue().set(FreightCategoryEnum.LAUNDRY.getKey(),value);
        freightSet = new FreightSet();
        freightSet.setKey(FreightCategoryEnum.MALL.getKey());
        freightSet.setFreight(5000L);
        freightSet.setFreight(2000L);
        value = JSON.toJSONString(freightSet);
        redisTemplate.opsForValue().set(FreightCategoryEnum.MALL.getKey(),value);
    }

    @Override
    public Commission getCommission(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        JSONObject commission = (JSONObject) JSON.parse(value);
        return JSONObject.toJavaObject(commission, Commission.class);
    }

    @Override
    public List<Commission> getAll() {
        List<Commission> commissions = new ArrayList<>();
        for (String key : Arrays.asList("Laundry", "HighLaundry", "Furniture", "Mall")) {
            String value = (String) redisTemplate.opsForValue().get("commission" + key);
            JSONObject commission = (JSONObject) JSON.parse(value);
            commissions.add(JSONObject.toJavaObject(commission, Commission.class));
        }
        return commissions;
    }
}
