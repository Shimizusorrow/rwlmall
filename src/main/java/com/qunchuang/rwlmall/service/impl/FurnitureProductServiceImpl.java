package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.FurnitureProductRepository;
import com.qunchuang.rwlmall.service.FurnitureProductService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/3/13 8:51
 */

@Service
public class FurnitureProductServiceImpl extends ProductServiceImpl<FurnitureProduct> implements FurnitureProductService {

    @Autowired
    private FurnitureProductRepository furnitureProductRepository;


    @Override
    public List<FurnitureProduct> findByStatus(Integer status) {
        List<FurnitureProduct> list = furnitureProductRepository.findByStatus(status);
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(FurnitureOrder furnitureOrder) {
        for (FurnitureOrderItem furnitureOrderItem : furnitureOrder.getItems()) {
            FurnitureProduct furnitureProduct = furnitureOrderItem.getFurnitureProduct();
            Integer result = furnitureProduct.getStock() + furnitureOrderItem.getCount();
            furnitureProduct.setStock(result);
            furnitureProductRepository.save(furnitureProduct);
        }
    }



    @Override
    public Page<FurnitureProduct> findByStatus(Integer status, Pageable pageable) {
        return furnitureProductRepository.findByStatus(status,pageable);
    }

    @Override
    public synchronized Boolean judgeStock(FurnitureOrder furnitureOrder) {
        for (FurnitureOrderItem furnitureOrderItem : furnitureOrder.getItems()) {
            FurnitureProduct furnitureProduct = findOne(furnitureOrderItem.getFurnitureProduct().getId());
            Integer result =  furnitureProduct.getStock() - furnitureOrderItem.getCount();
            if (result < 0) {
                /*库存不足*/
               return true;
            }
        }
        return false;
    }

    @Override
    public List<FurnitureProduct> findAll() {
        return furnitureProductRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void decreaseStock(FurnitureOrder furnitureOrder) {
        for (FurnitureOrderItem furnitureOrderItem : furnitureOrder.getItems()) {
            FurnitureProduct furnitureProduct = furnitureOrderItem.getFurnitureProduct();
            Integer result = furnitureOrderItem.getFurnitureProduct().getStock() - furnitureOrderItem.getCount();
            if (result < 0) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
            }
            furnitureProduct.setStock(result);
            furnitureProductRepository.save(furnitureProduct);
        }
    }


}
