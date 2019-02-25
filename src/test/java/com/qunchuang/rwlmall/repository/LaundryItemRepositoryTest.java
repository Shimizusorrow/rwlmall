package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.Action;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/7/3 8:42
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class LaundryItemRepositoryTest {

    @Autowired
    private LaundryItemRepository laundryItemRepository;

    @Test
    public void findByBarCode() throws Exception {
        List<LaundryOrderItem> laundryOrderItem = laundryItemRepository.findByBarCode("111");
        System.out.println(laundryOrderItem);
    }

}