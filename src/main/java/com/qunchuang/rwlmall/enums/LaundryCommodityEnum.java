package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/17 9:12
 */

@Getter
public enum LaundryCommodityEnum {

    NOT_INBOUND(0, "未入站"),
    INBOUND(1,"入站"),
    HANG_ON(2,"上挂");

    private Integer code;
    private String message;

    LaundryCommodityEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
