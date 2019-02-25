package com.qunchuang.rwlmall.utils;

import com.qunchuang.rwlmall.domain.Order;

import java.util.Comparator;

/**
 * @author Curtain
 * @date 2018/4/20 9:34
 */
public class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        //按时间降序排列
        if (o1.getCreatetime()<o2.getCreatetime()){
            return 1;
        }else {
            return -1;
        }
    }
}
