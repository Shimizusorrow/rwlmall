package com.qunchuang.rwlmall.enums;

import lombok.Getter;


@Getter
public enum AddressEnum {
    DEFAULT(0,"默认地址"),
    UN_DEFAULT(1,"非默认地址");


    private Integer code;
    private String msg;
    AddressEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

}