package com.qunchuang.rwlmall.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/3/29 14:49
 * 用户充值消费记录
 */

@Getter
@Setter
public class UserRecord {

    /*时间*/
    private String time;

    /*类目*/
    private String category;

    /*金额 单位分*/
    private Long  money;

    /*账户余额 单位分*/
    private Long  balance;

    /*订单号  查看详细*/
    private String orderId;

}
