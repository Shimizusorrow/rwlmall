package com.qunchuang.rwlmall.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.Entry;
import com.qunchuang.rwlmall.enums.LaundryCommodityEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Curtain
 * @date 2018/4/8 9:14
 */

@Entity
@Bostype("A14")
@Table(name = "T_FurnitureOrderItem")
@Setter
@Getter
public class FurnitureOrderItem extends Entry{

    /*商品*/
    @ManyToOne(fetch= FetchType.LAZY)
    private FurnitureProduct furnitureProduct;

    /*商品数量*/
    private Integer count;

    /*规格*/
    private String standard;

    /*图片*/
    private String image;

    /*状态 是否上传图片 （入站）*/
    private Integer status = LaundryCommodityEnum.NOT_INBOUND.getCode();


    @ManyToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JsonBackReference
    public FurnitureOrder getParent() {
        return (FurnitureOrder)super.getInnerParent();
    }


}
