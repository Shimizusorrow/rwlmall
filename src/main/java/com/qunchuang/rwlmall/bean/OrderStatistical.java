package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Curtain
 * @date 2018/4/23 14:23
 * 订单分析
 */

@Getter
@Setter
public class OrderStatistical {

    /*类目*/
    private String category;

    /*订单数*/
    private Integer orderCount;

    /*单价平均*/
    private Integer unitPrice;

    /*商品数量*/
    private Integer number;

    /*微信支付*/
    private Integer weChatPay;

    /*余额支付*/
    private Integer balancePay;

    /*卡支付*/
    private Integer cardPay;

}
