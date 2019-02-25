package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/28 14:39
 */

@Getter
public enum PlatformTextEnum {

    COMMON_PROBLEM(1,"常见问题"),
    ABOUT(2,"关于小让"),
    JOIN_IN(3,"加盟小让"),
    USER_PROTOCOL(4,"用户协议"),
    ACCEDE(5,"加入小让");

    private Integer code;

    private String message;

    PlatformTextEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
