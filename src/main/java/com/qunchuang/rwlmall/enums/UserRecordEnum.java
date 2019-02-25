package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/3/29 14:48
 */

@Getter
public enum UserRecordEnum {

    CONSUME(0,"消费"),
    RECHARGE(1,"充值"),
    REFUND(2,"退款"),
    GUIDE_CARD(3,"导卡"),
    CHARGE_BACK(4,"扣款")
    ;

    private Integer code;

    private String message;

    UserRecordEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
