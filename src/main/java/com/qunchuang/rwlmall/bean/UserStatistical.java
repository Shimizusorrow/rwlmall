package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Curtain
 * @date 2018/4/27 16:21
 * 会员人数统计
 */

@Setter
@Getter
public class UserStatistical {

    /*全部会员*/
    private Integer allCount;

    /*消费会员*/
    private Integer consumeCount;

    /*未消费会员*/
    private Integer unConsumeCount;

    /*储蓄会员*/
    private Integer depositCount;

    /*会员卡绑定用户*/
    private Integer memberCount;
}
