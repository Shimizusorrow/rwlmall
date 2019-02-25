package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/6/5 9:40
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LaundryOrderRepositoryTest {

    @Autowired
    private LaundryOrderRepository repository;


    @Test
    public void findByItemBarCode() throws Exception {
//        LaundryOrder byItemBarCode = repository.findByItemBarCode("222");
//        System.out.println(byItemBarCode);

    }

}