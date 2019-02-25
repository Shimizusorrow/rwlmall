package com.qunchuang.rwlmall.domain;



import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.ConsumeStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Bostype("A02")
@Table(name = "T_User")
@Getter
@Setter
public class User extends BaseEntity {

    /*姓名*/
    private String name;

    /*微信openid*/
    private String openid;

    /*性别*/
    @Column(length = 10)
    private String gender;

    /*国家*/
    private String country;

    /*省份*/
    private String province;

    /*城市*/
    private String city;

    /*头像*/
    private String headImgUrl;

    /*手机号*/
    private String phone;

    /*余额*/
    private long balance;

    /*注册手机号*/
    private String  registerPhone;

    /*消费次数*/
    private Integer frequency = 0;

    /*消费状态*/
    private Integer consumeStatus = ConsumeStatusEnum.NOT_CONSUME.getCode();

    /*是否储值*/
    private Boolean deposit = false;

    /*是否绑定手机号*/
    private Boolean binding = false;

    /*是否绑定卡*/
    private Boolean bindingCard = false;

    /*卡号*/
    private String cid;

    /*卡编码*/
    private String cno;

    /*卡折扣*/
    private Integer discount;
}