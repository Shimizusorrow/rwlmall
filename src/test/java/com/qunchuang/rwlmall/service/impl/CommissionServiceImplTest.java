package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.Commission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/4/18 15:49
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CommissionServiceImplTest {

    @Autowired
    private CommissionServiceImpl commissionService;

    @Test
    public void setCommission() throws Exception {
        Commission commission = new Commission();
        commission.setKey("commissionMall");
        commission.setAgent(20);
        commission.setExpress(10);
        commission.setPlatform(70);

        commissionService.setCommission("commissionMall",commission);
    }

}