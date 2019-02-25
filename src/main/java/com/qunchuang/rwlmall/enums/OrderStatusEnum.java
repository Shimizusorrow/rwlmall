package com.qunchuang.rwlmall.enums;

import lombok.Getter;

@Getter
public enum  OrderStatusEnum{

    WAIT_PAY(-1,"待支付新订单"),
    NEW(0, "新订单"),
    DELIVERY(1,"已派订单"),
    INBOUND(3,"入站订单"),
    HANG_ON(4,"上挂订单"),
    GIVE_BACK(7,"送还订单"),
    FINISHED(5,"完结订单"),
    CANCEL(6,"取消订单");

    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
