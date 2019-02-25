package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * 运费类别
 *
 * @author Curtain
 * @date 2018/12/4 8:22
 */
@Getter
public enum FreightCategoryEnum {

    /*运费信息key*/
    LAUNDRY("洗衣", "rwlFreightSet"),
    MALL("商城", "mallFreightSet");


    private String category;
    private String key;

    FreightCategoryEnum(String category, String key) {
        this.category = category;
        this.key = key;
    }

}
