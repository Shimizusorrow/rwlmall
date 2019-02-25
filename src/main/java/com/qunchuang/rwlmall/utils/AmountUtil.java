package com.qunchuang.rwlmall.utils;

/**
 * @author Curtain
 * @date 2018/4/23 15:29
 */
public class AmountUtil {

    /*分 to 元*/
    public static Integer fenToYuan(Long amount){
        return Integer.valueOf((int) (amount/100));
    }

}
