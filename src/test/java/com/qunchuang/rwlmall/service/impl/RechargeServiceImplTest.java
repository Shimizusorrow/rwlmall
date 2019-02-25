package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.service.RechargeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 13:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RechargeServiceImplTest {


    @Autowired
    private RechargeService rechargeService;

    @Test
    public void setRecharge() throws Exception {
        Recharge recharge = new Recharge();
        recharge.setKey("rwlRecharge3");
        recharge.setPayMoney(200000l);
        recharge.setRewardMoney(50000l);
        rechargeService.setRecharge("rwlRecharge3",recharge);
    }

    @Test
    public void getRecharge() throws Exception {
        List<Recharge> recharges = rechargeService.getAll();
        for (Recharge recharge : recharges){
            System.out.println(recharge);
        }
    }

}