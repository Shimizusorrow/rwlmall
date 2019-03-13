package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/5/10 15:51
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticalServiceImplTest {

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Autowired
    private FurnitureOrderService furnitureOrderService;

    @Autowired
    private MallOrderService mallOrderService;


}