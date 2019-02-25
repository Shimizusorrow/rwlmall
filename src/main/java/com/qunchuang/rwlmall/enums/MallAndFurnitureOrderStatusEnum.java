package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/5/5 8:58
 */

@Getter
public enum MallAndFurnitureOrderStatusEnum {

    NEW(0, "新订单"),
    DELIVERY(1,"已派订单"),
    SEND(4,"已发订单"),
    FINISHED(5,"完结订单"),
    CANCEL(6,"取消订单");

    private Integer code;
    private String message;

    MallAndFurnitureOrderStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
