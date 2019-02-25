package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/14 12:55
 */

@Getter
public enum LaundryOrderTimeEnum {

    NORMAL(0,"正常"),
    TIME_OUT(1,"超时");

    private Integer code;
    private String msg;

    LaundryOrderTimeEnum(Integer code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
