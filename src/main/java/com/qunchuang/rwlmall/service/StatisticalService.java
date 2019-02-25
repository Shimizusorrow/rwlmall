package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.*;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.Order;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author Curtain
 * @date 2018/4/23 14:16
 */
public interface StatisticalService {

    /*订单分析统计*/
    List<OrderStatistical> orderAnalysis(Long startTime, Long endTime, String province, String city, String area,Principal principal);

    /*按时间统计新订单件数*/
    Map<String, Integer> newOrderCount(Long startTime, Long endTime, String province, String city, String area, Principal principal);

    /*会员统计*/
    UserStatistical userCountStatistical(Long startTime, Long endTime, String country, String province, String city);

    /*商户结算*/
    List<StoreStatistical> storeAnalysis(Long startTime, Long endTime, String province, String city, String area,Principal principal);

    /*门店每日结算(商户结算详情)*/
    Map<String, Object> storeDayAnalysis(String storeId, Integer page);

    /*门店查看自身所有新订单*/
    List<Order> allInboundStoreOrder(String storeId);

    /*门店通过number或者手机号查询新订单*/
    List<Order> storeFindNewOrder(String storeId,String key,Integer type);

    /*门店查看准备上挂订单*/
    List<LaundryOrder> allHangOnStoreOrder(String storeId);

    /*门店所属的所有未完结订单*/
    List<Object> allStoreOrder(String storeId,Integer type);

    /*洗衣消费统计*/
    Map<String, List> laundryConsumeStatistical(Long startTime, Long endTime, String province, String city, String area, Integer type);

    /*家具消费统计*/
    Map<String, List> furnitureConsumeStatistical(Long startTime, Long endTime, String province, String city, String area);

    /*商城消费统计*/
    Map<String, List> mallConsumeStatistical(Long startTime, Long endTime, String province, String city, String area);

    /*洗衣商品统计*/
    Map<String,Object> laundryProductStatistical(Long startTime, Long endTime, String province, String city, String area,Integer type);

    /*家具商品统计*/
    Map<String,Object> furnitureProductStatistical(Long startTime, Long endTime, String province, String city, String area);

    /*商城商品统计*/
    Map<String, Object> mallProductStatistical(Long startTime, Long endTime, String province, String city, String area);

    /*收支明细统计*/
    FinanceStatistical financeStatistical(Long startTime,Long endTime);




}
