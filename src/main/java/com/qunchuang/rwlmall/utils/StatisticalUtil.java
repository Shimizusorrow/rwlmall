package com.qunchuang.rwlmall.utils;

import java.util.List;
import java.util.TreeMap;

/**
 * @author Curtain
 * @date 2018/5/15 8:40
 * 统计工具类
 */
public class StatisticalUtil {

    public final static int[] MONEY_INTERVAL = {10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000};

    public static void initConsumeResultData(List<Integer> consumeFrequencyList, List<Integer> consumePriceList) {

        //为每个次数区间初始化数据
        for (int i = 0; i < 12; i++) {
            consumeFrequencyList.add(0);
        }

        //为每个金额区间初始化数据
        for (int i = 0; i < 11; i++) {
            consumePriceList.add(0);
        }
    }
}
