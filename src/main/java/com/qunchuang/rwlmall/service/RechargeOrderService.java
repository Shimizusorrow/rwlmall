package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.RechargeOrder;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/16 10:04
 */
public interface RechargeOrderService {

    /*创建充值订单*/
    RechargeOrder create(String rechargeKey, String userId);

    /*根据id查找一条充值记录*/
    RechargeOrder findOne(String orderId);

    /*支付订单*/
    RechargeOrder paid(String orderId,Integer payMode);

    /*按时间查询支付成功的订单*/
    List<RechargeOrder> findByTimeAndPayStatus(Long startTime,Long endTime,Integer payStatus);
}
