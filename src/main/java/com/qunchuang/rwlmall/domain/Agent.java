package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.StoreStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/3/13 14:00
 */

@Entity
@Bostype("A07")
@Table(name = "T_Agent")
@Getter
@Setter
public class Agent extends BaseEntity{

    /*联系电话*/
    @Column(length = 20)
    private String phone;

    /*代理商名称*/
    private String agentName;

    /*地区负责人*/
    @Column(length = 20)
    private String people;

    /*办公地址*/
    @Column(length = 100)
    private String address;

    /*QQ*/
    @Column(length = 20)
    private String qq;

    /*办公时间*/
    @Column(length = 100)
    private String workTime;

    /*区域分配*/
    private String regionDistribution;

    /*可收区域*/
    private String region;

    /*账号*/
    private boolean account;

    /*登录账号名*/
    private String accountName;

}
