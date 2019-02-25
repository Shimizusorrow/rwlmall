package com.qunchuang.rwlmall.domain;

/**
 * @author Curtain
 * @date 2018/4/13 10:49
 */
public interface Order {

    String getUserId();

    String getId();

    long getAmount();

    long getCreatetime();

    Integer getStatus();

    Integer getPayStatus();

    String getNumber();

    String getPhone();

    long getCardAmount();

    void setCardAmount(long cardAmount);


}
