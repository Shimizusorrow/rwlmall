package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.enums.LaundryCategoryEnum;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import com.qunchuang.rwlmall.enums.ProductTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/13 15:36
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class LaundryLaundryProductServiceImplTest {

    @Autowired
    private LaundryProductServiceImpl laundryProductService;

    @Test
    public void findOne() throws Exception {
        LaundryProduct laundryProduct = laundryProductService.findOne("-tCzD0E-GiizIvMBpGcu71A01");
        Assert.assertNotNull(laundryProduct);
    }

    @Test
    public void findByNumber() throws Exception {
        LaundryProduct laundryProduct = laundryProductService.findByNumber("523238964132");
        Assert.assertNotNull(laundryProduct);
    }

    @Test
    public void findByType() throws Exception {
        List<LaundryProduct> laundryProducts = laundryProductService.findByType(ProductTypeEnum.LAUNDRY.getCode());
        Assert.assertNotNull(laundryProducts);
    }


    @Test
    public void findByTypeAndCategory() throws Exception {
        List<LaundryProduct> laundryProducts = laundryProductService.findByTypeAndCategory(ProductTypeEnum.LAUNDRY.getCode(), 1);
        Assert.assertNotNull(laundryProducts);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void save() throws Exception {
        LaundryProduct laundryProduct = new LaundryProduct();
        laundryProduct.setStatus(ProductStatusEnum.UP.getCode());
        laundryProduct.setCategory(LaundryCategoryEnum.SHOES_BAG.getCode());
        laundryProduct.setType(ProductTypeEnum.LAUNDRY.getCode());
        laundryProduct.setName("皮鞋2");
        laundryProduct.setPrice(9999l);
        laundryProduct.setStatus(ProductStatusEnum.UP.getCode());
        laundryProduct.setStock(9999);
        laundryProduct.setOldPrice(99999l);
        laundryProduct.setDate(11111l);
        LaundryProduct product = laundryProductService.save(laundryProduct);
        Assert.assertNotNull(product);

    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void update() throws Exception {
        LaundryProduct laundryProduct = new LaundryProduct();
        laundryProduct.setStatus(ProductStatusEnum.UP.getCode());
        laundryProduct.setCategory(1);
        laundryProduct.setType(ProductTypeEnum.LAUNDRY.getCode());
        laundryProduct.setName("大衣");
        laundryProduct.setPrice(998l);
        laundryProduct.setOldPrice(18000l);
        laundryProduct.setDate(11111l);
        laundryProductService.update("-tCzD0E-GiizIvMBpGcu71A01",laundryProduct);

    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock() throws Exception {
//        LaundryOrderItem laundryOrderItem = new LaundryOrderItem();
//        laundryOrderItem.setLaundryProduct(laundryProductService.findOne("-tCzD0E-GiizIvMBpGcu71A01"));
//        laundryOrderItem.setCount(10);
//        HashSet<LaundryOrderItem> laundryOrderItems = new HashSet<>();
//        laundryOrderItems.add(laundryOrderItem);
//        laundryProductService.increaseStock(laundryOrderItems);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void upOffShelf() throws Exception {
        laundryProductService.upOffShelf("VyIkn0dqFFSccEyMVhJIb1A01");
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void decreaseStock() throws Exception {
//        LaundryOrderItem laundryOrderItem = new LaundryOrderItem();
//        laundryOrderItem.setLaundryProduct(laundryProductService.findOne("VyIkn0dqFFSccEyMVhJIb1A01"));
//        laundryOrderItem.setCount(1000);
//        HashSet<LaundryOrderItem> laundryOrderItems = new HashSet<>();
//        laundryOrderItems.add(laundryOrderItem);
//        laundryProductService.decreaseStock(laundryOrderItems);
    }

}