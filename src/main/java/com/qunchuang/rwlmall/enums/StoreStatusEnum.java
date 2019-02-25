package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/3/13 14:14
 */

@Getter
public enum StoreStatusEnum {

    BUSINESS(0,"营业中"),
    UN_BUSINESS(1,"未开业")
    ;

    private Integer code;

    private String message;

    StoreStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
