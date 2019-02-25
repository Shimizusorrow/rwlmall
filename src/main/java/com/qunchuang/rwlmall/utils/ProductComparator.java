package com.qunchuang.rwlmall.utils;

import com.qunchuang.rwlmall.domain.Product;

import java.util.Comparator;

/**
 * @author Curtain
 * @date 2018/7/12 10:51
 */
public class ProductComparator implements Comparator<Product> {

    /**
     * 通过sort 进行降序排列
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Product o1, Product o2) {
        if (o1.getSort()<o2.getSort()){
            return 1;
        }else {
            return -1;
        }
    }
}
