package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

/**
 * @author Curtain
 * @date 2018/5/14 9:12
 * 消费人数统计
 */

@Getter
@Setter
public class ConsumeStatistical {

    /*用户*/
    private String userId;

    /*消费总金额 单位：分*/
    private Long money;

    /*次数*/
    private Integer count = 0;
}
