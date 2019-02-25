package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/4/9 14:39
 * 提成机制
 */

@Getter
@Setter
public class Commission {

    /*键*/
    private String key;

    /*代理商   单位:%*/
    private Integer agent;

    /*物流   单位:%*/
    private Integer express;

    /*平台   单位:%*/
    private Integer platform;

}
