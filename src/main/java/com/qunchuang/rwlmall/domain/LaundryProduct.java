package com.qunchuang.rwlmall.domain;


import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Bostype("A01")
@Table(name = "T_LaundryProduct")
@Getter
@Setter
public class LaundryProduct extends BaseEntity implements Product {

    /*名称*/
    private String name;

    /*原价  单位分*/
    private long oldPrice;

    /*现价  单位分*/
    private long price;

    /*商品图片*/
    private String logo;

    /*商品库存*/
    private int stock;

    /*商品生效日期*/
    private long date;

    /*商品排序（显示优先级）*/
    private int sort;

    /*商品类别*/
    private Integer category;

    /*商品所属类型*/
    private Integer type;

    /*商品是否上架状态*/
    private Integer status = ProductStatusEnum.DOWN.getCode();

    /*是否自动上架*/
    private boolean autoStatus = false;
}
