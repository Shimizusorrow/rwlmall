package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/6/5 9:50
 */
@Getter
public enum QueryTypeEnum {

    COMMON_PROBLEM(1,"number查询"),
    ABOUT(2,"手机号查询");


    private Integer code;

    private String message;

    QueryTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
