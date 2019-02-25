package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/4/9 10:45
 * 充值奖励机制
 */

@Getter
@Setter
public class Recharge {

    /*键*/
    private String key;

    /*充值金额*/
    private long payMoney;

    /*奖励金额*/
    private long rewardMoney;
}
