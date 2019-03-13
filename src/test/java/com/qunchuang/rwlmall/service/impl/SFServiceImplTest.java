package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.ExpressInfo;
import com.qunchuang.rwlmall.service.SFService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 16:41
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class SFServiceImplTest {

    @Autowired
    private SFService sfService;

//    @Test
//    public void dispatchOrder() throws Exception {
//        String s = sfService.dispatchOrder("xx",0);
//        System.out.println(s);
//    }

    @Test
    public void routingQuery() throws Exception {
        List<ExpressInfo> list = sfService.routingQuery("Xx");
        System.out.println(list);
    }

}