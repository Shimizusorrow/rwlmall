package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * @author Curtain
 * @date 2018/5/14 8:57
 * 商品销售统计
 */

@Getter
@Setter
public class ProductStatistical {

    /*商品名称*/
    private String productName;

    /*销售数量*/
    private Integer count = 0;

    /*类别*/
    private Integer category;


}
