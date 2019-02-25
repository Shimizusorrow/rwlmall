package com.qunchuang.rwlmall.bos;

/**
 * Created by liutim on 2017/11/25.
 */
public interface IEntity extends ICoreObject{

     String getCreateactorid();

     String getUpdateactorid();

     long getCreatetime();

     long getUpdatetime();
}

