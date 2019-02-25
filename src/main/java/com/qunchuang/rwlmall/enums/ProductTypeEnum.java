package com.qunchuang.rwlmall.enums;

import lombok.Getter;

@Getter
public enum  ProductTypeEnum {


    LAUNDRY(1,"洗衣"),
    HIGH_LAUNDRY(2,"高端洗护");

    private Integer code;

    private String message;

    ProductTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
