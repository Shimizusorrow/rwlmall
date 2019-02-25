package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.StoreStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Curtain
 * @date 2018/4/17 10:02
 */

@Entity
@Bostype("A16")
@Table(name = "T_Store")
@Getter
@Setter
public class Store extends BaseEntity {

    /*门店名称*/
    private String name;

    /*联系电话*/
    @Column(length = 20)
    private String phone;

    /*营业时间*/
    private String openTime;

    /*详细地址*/
    @Column(length = 100)
    private String address;

    /*省份*/
    @Column(length = 20)
    private String province;

    /*城市*/
    @Column(length = 20)
    private String city;

    /*区域*/
    @Column(length = 20)
    private String area;

    /*代理商id*/
    private String agentId;

    /*状态*/
    private Integer status = StoreStatusEnum.UN_BUSINESS.getCode();

    /*是否有账号*/
    private boolean account;

    /*登录账号名*/
    private String accountName;

    /*负责人*/
    private String people;
}
