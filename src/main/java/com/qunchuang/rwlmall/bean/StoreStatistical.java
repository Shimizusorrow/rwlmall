package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/4/26 14:04
 * 商戶結算
 */

@Getter
@Setter
public class StoreStatistical {

    /*id*/
    private String id;

    /*名称*/
    private String name;

    /*订单总数*/
    private Integer orderTotal;

    /*商品数量*/
    private Integer productTotal;

    /*收入总额*/
    private Long incomeTotal;

    /*充值总额*/
    private Long rechargeTotal;

    /*退款总额*/
    private Long refundTotal;

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
}
