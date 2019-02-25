package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.Recharge;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 10:47
 */
public interface RechargeService {

    /*设置充值奖励*/
    void setRecharge(String key, Recharge recharge);

    /*获取充值奖励列表*/
    List<Recharge> getAll();

    /*获取指定充值奖励*/
    Recharge getRecharge(String key);
}
