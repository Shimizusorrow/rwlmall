package com.qunchuang.rwlmall.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.Entry;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Curtain
 * @date 2018/4/8 9:16
 */

@Entity
@Bostype("A11")
@Table(name = "T_MallOrderItem")
@Setter
@Getter
public class MallOrderItem extends Entry{

    /*商品*/
    @ManyToOne(fetch= FetchType.LAZY)
    private MallProduct mallProduct;

    /*商品数量*/
    private Integer count;

    /*规格*/
    private String standard;


    @ManyToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JsonBackReference
    public MallOrder getParent() {
        return (MallOrder)super.getInnerParent();
    }



}
