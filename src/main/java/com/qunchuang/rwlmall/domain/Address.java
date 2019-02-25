package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.AddressEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Bostype("A05")
@Table(name = "T_Address")
@Getter
@Setter
public class Address extends BaseEntity {

    /*用户id*/
    @Column(length = 40)
    private String userId;

    /*联系电话*/
    @Column(length = 20)
    private String phone;

    /*收件人*/
    @Column(length = 20)
    private String consignee;

    /*省份*/
    @Column(length = 20)
    private String province;

    /*城市*/
    @Column(length = 20)
    private String city;

    /*区域*/
    @Column(length = 20)
    private String area;

    /*详细收货地址*/
    @Column(length = 100)
    private String address;

    /*是否为默认*/
    private Integer status= AddressEnum.UN_DEFAULT.getCode();


}
