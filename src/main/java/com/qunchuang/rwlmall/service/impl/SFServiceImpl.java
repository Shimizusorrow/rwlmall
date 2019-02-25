package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.ExpressInfo;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.Store;
import com.qunchuang.rwlmall.enums.SFStatusEnum;
import com.qunchuang.rwlmall.service.*;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 顺丰快递收发 删除  不支持 /2019-2-25
 * @author Curtain
 * @date 2018/3/27 8:31
 */

@Service
public class SFServiceImpl implements SFService {
    @Override
    public String dispatchOrder(String orderId,String storeId, Integer status) {
        return null;
    }


    @Override
    public List<ExpressInfo> routingQuery(String orderId) {
        return null;
    }
}
