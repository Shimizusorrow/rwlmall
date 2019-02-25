package com.qunchuang.rwlmall.utils;

import java.util.Random;

public class KeyUtil {

    //生成唯一的主键
    //  格式：时间+随机数

    public static synchronized String genUniqueKey(){
        Random random = new Random();

        System.currentTimeMillis();

        Integer number = random.nextInt(9000)+1000;

        return System.currentTimeMillis()+String.valueOf(number);
    }

}
