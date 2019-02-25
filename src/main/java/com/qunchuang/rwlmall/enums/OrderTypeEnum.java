package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/3/12 9:31
 */

@Getter
public enum OrderTypeEnum {

    LAUNDRY(1,"洗衣"),
    HIGH_LAUNDRY(2,"高端洗护");


    private Integer code;

    private String message;

    OrderTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
