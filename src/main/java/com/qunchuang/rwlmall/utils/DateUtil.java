package com.qunchuang.rwlmall.utils;

import java.util.Calendar;

/**
 * @author Curtain
 * @date 2018/5/23 10:38
 */
public class DateUtil {

    public final static Long DAY_TIME_STAMP=86400000L;

    /*获取当日凌晨毫秒数*/
    public static Long getDateTimeStamp(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /*根据毫秒数获取日期*/
    public static String getDate(Long timeStamp){
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(timeStamp);
        int year = calendar2.get(Calendar.YEAR);
        int month = calendar2.get(Calendar.MONTH)+1;
        int day = calendar2.get(Calendar.DAY_OF_MONTH);

        return year+"."+month+"."+day;
    }

    public static String currentTime(){
        return String.valueOf(System.currentTimeMillis());
    }
}
