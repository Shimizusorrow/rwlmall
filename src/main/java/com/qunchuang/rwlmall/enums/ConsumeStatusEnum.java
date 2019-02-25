package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/27 15:28
 */

@Getter
public enum ConsumeStatusEnum {

    NOT_CONSUME(0,"未消费"),
    CONSUME(1,"已消费");



    private Integer code;
    private String msg;
    ConsumeStatusEnum(Integer code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
