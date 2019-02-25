package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.FurnitureProduct;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.enums.LaundryOrderTimeEnum;
import com.qunchuang.rwlmall.enums.OrderStatusEnum;
import com.qunchuang.rwlmall.enums.OrderTypeEnum;
import com.qunchuang.rwlmall.utils.CardPayUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/5/3 9:46
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Test
    public void save() throws Exception {
//        List<LaundryOrder> byTypeAndStatus = laundryOrderService.findByTypeAndStatus(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.GIVE_BACK.getCode());
//
//        for (LaundryOrder laundryOrder : byTypeAndStatus){
//            if (laundryOrder.getNumber().equals("538099343618")){
//                System.out.println(laundryOrder);
//            }
//        }
//        System.out.println(byTypeAndStatus);
        List<LaundryOrder> byTimeOutAndType = laundryOrderService.findByTimeOutAndType(LaundryOrderTimeEnum.TIME_OUT.getCode(), OrderTypeEnum.LAUNDRY.getCode());
        System.out.println(byTimeOutAndType);
    }
}