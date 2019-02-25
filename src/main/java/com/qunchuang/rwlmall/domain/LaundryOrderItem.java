package com.qunchuang.rwlmall.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.Entry;
import com.qunchuang.rwlmall.enums.LaundryCommodityEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Bostype("A04")
@Table(name = "T_LaundryOrderItem")
@Setter
@Getter
public class LaundryOrderItem extends Entry {

    /*商品*/
    @ManyToOne(fetch=FetchType.LAZY)
    private LaundryProduct laundryProduct;

    /*商品数量*/
    private Integer count;

    /*衣服条形码*/
    private String barCode;

    /*洗后效果*/
    private String washingEffect;

    /*瑕疵*/
    private String flaw;

    /*备注*/
    private String remark;

    /*图片*/
    private String image;

    /*问题图片*/
    private String problemImage;

    /*状态 未入站 or 入站 or 上挂*/
    private Integer status = LaundryCommodityEnum.NOT_INBOUND.getCode();

    @ManyToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JsonBackReference
    public LaundryOrder getParent() {
        return (LaundryOrder)super.getInnerParent();
    }




}
