package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Curtain
 * @date 2018/7/2 15:13
 * 雄伟卡信息
 */

@Getter
@Setter
@ToString
public class CardInfo {

    /*卡号*/
    private String cid;

    /*交易卡面编码*/
    private String cno;

    /*卡余额*/
    private Long amount;

    /*手机号*/
    private String mobile;

    /*卡类型*/
    private String type;

    /**
     * 卡状态   1: 正常 2: 校验失败 3: 已挂失 4: 已作废 5: 过期
     */
    private Integer status;

    /**
     * 卡锁定  0表示未锁定  1表示锁定  2表示冻结
     */
    private Integer locked;

    /**
     * 卡折扣    100表示 100%  不打折
     */
    private Integer discount;


}
