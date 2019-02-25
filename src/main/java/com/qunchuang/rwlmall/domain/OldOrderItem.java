package com.qunchuang.rwlmall.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.Entry;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Curtain
 * @date 2018/9/3 9:21
 */

@Entity
@Bostype("A28")
@Table(name = "T_OldOrderItem")
@Setter
@Getter
public class OldOrderItem extends Entry {

    @ManyToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JsonBackReference
    public OldOrder getParent() {
        return (OldOrder) super.getInnerParent();
    }

    /**
     * 描述
     */
    private String name;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 价格
     */
    private Integer price;
}