package com.qunchuang.rwlmall.service;


import com.lly835.bestpay.model.PayResponse;
import com.qunchuang.rwlmall.bean.WeChatShare;

public interface PayService {

    /*微信支付*/
    PayResponse weChatPay(String orderId);

    /*余额支付*/
    void balancePay(String orderId);

    /*实体卡支付*/
    void cardPay(String orderId, String userId);

    /*异步通知回调*/
    PayResponse notify(String notifyData);

    /**
     * 生成微信分享参数
     * @return
     */
    WeChatShare generate();


/*    //微信退款
    RefundResponse refund(String orderId);*/
}
