package com.qunchuang.rwlmall.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/3/17 10:53
 */

@Setter
@Getter
public class ResultVO<T> {

    //错误码
    private Integer code;

    //提示信息
    private String msg;

    //具体内容
    private T data;

    @Override
    public String toString() {
        return super.toString();
    }
}
