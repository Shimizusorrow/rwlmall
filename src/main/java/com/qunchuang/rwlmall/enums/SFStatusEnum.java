package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/9 17:16
 */

@Getter
public enum SFStatusEnum {

    STORE(2,"门店收单"),
    SF(3,"顺丰收单"),
    COLLECT(0,"收"),
    SEND(1,"派")
    ;

    private Integer code;

    private String message;

    SFStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
