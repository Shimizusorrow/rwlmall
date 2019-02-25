package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/3/28 16:27
 */

@Getter
public enum  AdvertisementEnum {


    HOME_PAGE(0,"首页"),
    MALL(1,"小让商城");

    private Integer code;

    private String message;

    AdvertisementEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
