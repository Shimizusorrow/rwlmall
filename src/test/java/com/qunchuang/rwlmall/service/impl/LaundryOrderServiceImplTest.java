package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.service.LaundryOrderService;
import com.qunchuang.rwlmall.service.LaundryProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/4/17 14:51
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class LaundryOrderServiceImplTest {

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Autowired
    private LaundryProductService laundryProductService;

    @Test
    public void create() throws Exception {
        LaundryOrder laundryOrder = new LaundryOrder();
        LaundryOrderItem laundryOrderItem = new LaundryOrderItem();
        LaundryOrderItem laundryOrderItem2 = new LaundryOrderItem();
        Set<LaundryOrderItem> laundryOrderItems = new HashSet<>();
        laundryOrderItems.add(laundryOrderItem);
        laundryOrderItems.add(laundryOrderItem2);

        laundryOrderItem2.setCount(3);
        laundryOrderItem.setCount(5);
        LaundryProduct product = laundryProductService.findOne("AmLu1RmlFDaoX4-RKCpUj0A01");
        LaundryProduct product2 = laundryProductService.findOne("AmLu1RmlFDaoX4-RKCpUj0A01");
        laundryOrderItem2.setLaundryProduct(product2);
        laundryOrderItem.setLaundryProduct(product);
        laundryOrder.setItems(laundryOrderItems);

        laundryOrderService.create(laundryOrder,"FntQVbTIG5W8NPkYnkAdp2A02");
    }
}