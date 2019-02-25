package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.Commission;
import com.qunchuang.rwlmall.bean.Recharge;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 14:51
 */

public interface CommissionService {

    /*设置提成机制*/
    void setCommission(String key, Commission commission);

    /*获取所有提成机制*/
    List<Commission> getAll();

    /*获取单个提成机制*/
    Commission getCommission(String key);

    /*初始化redis数据*/
    void initRedisData();
}
