package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.ExpressInfo;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/27 8:15
 */
public interface SFService {

    /*派单*/
    String dispatchOrder(String orderId,String storeId,Integer status);

    /*路由查询*/
    List<ExpressInfo> routingQuery(String orderId);
}
