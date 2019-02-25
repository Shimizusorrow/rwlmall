package com.qunchuang.rwlmall.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/7/16 14:19
 * 用户消费记录
 */

@Entity
@Bostype("A21")
@Table(name = "T_ConsumeRecord")
@Getter
@Setter
public class ConsumeRecord extends BaseEntity{
    /*类目*/
    private String category;

    /*金额 单位分*/
    private Long  money;

    /*账户余额 单位分*/
    private Long  balance;

    /*订单号  查看详细*/
    private String orderId;

    /*用户id*/
    private String userId;

}
