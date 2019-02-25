package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.FreightSet;
import com.qunchuang.rwlmall.enums.FreightCategoryEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/5/15 11:04
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreightSetServiceImplTest {

    @Autowired
    private FreightSetServiceImpl freightSetService;

    @Test
    public void setFreightSet() throws Exception {
        FreightSet freightSet = new FreightSet();
        freightSet.setFreight(2000l);
        freightSet.setThreshold(5000l);
        freightSet.setKey(FreightCategoryEnum.LAUNDRY.getKey());
        freightSetService.setFreightSet(FreightCategoryEnum.LAUNDRY.getKey(), freightSet);
    }

}