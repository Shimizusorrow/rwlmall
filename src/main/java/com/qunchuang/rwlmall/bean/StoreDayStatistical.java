package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/5/23 10:24
 * 门店每日订单统计
 */

@Getter
@Setter
public class StoreDayStatistical {
    /*日期*/
    private String date;

    /*订单总数*/
    private Integer orderTotal;

    /*商品数量*/
    private Integer productTotal;

//    /*撤单数量*/
//    private Integer cancelOrderTotal;

    /*收入总额*/
    private Long incomeTotal;

//    /*退款总额*/
//    private Long refundTotal;

    /*洗衣*/
    private Long laundryTotal;

    /*高端洗护*/
    private Long highLaundryTotal;

    /*家具*/
    private Long furnitureTotal;

    /*小让商城*/
    private Long mallTotal;

    /*商户收入*/
    private Long agentIncome;

    /*平台收入*/
    private Long platformIncome;

    /*物流收入*/
    private Long expressIncome;

    /*微信支付*/
    private Long weChatPay;

    /*余额支付*/
    private Long balancePay;

    /*卡支付*/
    private Long cardPay;
}
