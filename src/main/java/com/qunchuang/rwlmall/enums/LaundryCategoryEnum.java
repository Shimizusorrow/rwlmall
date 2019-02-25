package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/8 15:16
 */

@Getter
public enum LaundryCategoryEnum {

    CLOTHES(0,"上衣类"),
    CULOTTES(1,"裤裙类"),
    FUR(2,"皮草类"),
    DECORATE(3,"装饰类"),
    SHOES_BAG(4,"鞋包类");

    private Integer code;
    private String msg;

    LaundryCategoryEnum(Integer code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
