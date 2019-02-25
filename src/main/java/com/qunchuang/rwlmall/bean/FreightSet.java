package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/5/15 10:44
 * 运费机制
 */

@Setter
@Getter
public class FreightSet {

    /*阈值  单位：分*/
    private Long threshold;

    /*运费  单位：分*/
    private Long freight;

    /**
     * key：FreightCategoryEnum
     */
    private String key;
}
