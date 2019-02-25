package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/8 15:11
 */

@Getter
public enum  MallCategoryEnum {

    DAILY_USE(0,"生活用品类"),
    SERVICE(1,"服务类"),
    ELECTRICAL_EQUIPMENT(2,"家电类"),
    SHOES(3,"鞋服类");

    private Integer code;
    private String msg;

    MallCategoryEnum(Integer code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
