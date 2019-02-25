package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/13 10:59
 */

@Getter
public enum OrderCategoryEnum {



    LAUNDRY_ORDER("A03","洗衣订单"),
    MALL_ORDER("A10","商城订单"),
    FURNITURE_ORDER("A13","家居订单"),
    RECHARGE_ORDER("A15","充值订单");

    private String message;

    private String key;


    OrderCategoryEnum(String key, String message){
        this.key = key;
        this.message = message;
    }
}
