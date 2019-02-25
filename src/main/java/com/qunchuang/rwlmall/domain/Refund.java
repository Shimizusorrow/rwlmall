package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/4/25 14:15
 */


@Entity
@Bostype("A19")
@Table(name = "T_Refund")
@Getter
@Setter
public class Refund extends BaseEntity {

    /*金额*/
    private Long money;

    /*持卡人姓名*/
    private String name;

    /*持卡人手机号*/
    private String phone;

    /*备注*/
    private String remark;

    /*会员id*/
    private String userNumber;

    /*绑定手机号*/
    private String userPhone;

    /*注册时间*/
    private Long userCreateTime;

    /*用户余额*/
    private Long oldBalance;

    /*转存人*/
    private String transferPeople;

    /*充值后余额*/
    private Long newBalance;
}
