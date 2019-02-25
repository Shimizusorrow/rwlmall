package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.FreightSet;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/5/15 10:51
 */
public interface FreightSetService {


    /*设置运费机制*/
    void setFreightSet(String key, FreightSet freightSet);

    /*获取运费信息*/
    FreightSet getFreightSet();

    /**
     * 获取运费信息
     * @param key
     * @return
     */
    FreightSet getFreightSet(String key);

    /**
     * 获取所有运费信息
     * @return
     */
    List<FreightSet> getAll();
}
