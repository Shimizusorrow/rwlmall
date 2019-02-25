package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/8 10:15
 */

@Getter
public enum PayModeEnum {

    WE_CHAT(0,"微信支付"),
    BALANCE(1,"余额支付"),
    CARD(2,"卡支付");

    private Integer code;
    private String msg;

    PayModeEnum(Integer code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
