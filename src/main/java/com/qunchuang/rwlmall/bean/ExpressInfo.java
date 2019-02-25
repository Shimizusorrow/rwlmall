package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/3/27 14:57
 * 快递路由查询结果
 */

@Getter
@Setter
public class ExpressInfo {

    /*状态*/
    private String state;

    /*时间*/
    private String time;

    /*地点*/
    private String address;
}
