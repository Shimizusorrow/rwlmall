package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.LaundryProductRepository;
import com.qunchuang.rwlmall.service.LaundryProductService;
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
public class LaundryProductServiceImpl extends ProductServiceImpl<LaundryProduct> implements LaundryProductService {

    @Autowired
    private LaundryProductRepository laundryProductRepository;


    @Override
    public Page<LaundryProduct> findByType(Pageable pageable, Integer type) {
        return laundryProductRepository.findByType(pageable, type);
    }

    @Override
    public List<LaundryProduct> findByType(Integer type) {
        return laundryProductRepository.findByType(type);
    }

    @Override
    public List<LaundryProduct> findByTypeAndCategory(Integer type, Integer category) {
        return laundryProductRepository.findByTypeAndCategory(type, category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(LaundryOrder laundryOrder) {
        for (LaundryOrderItem laundryOrderItem : laundryOrder.getItems()) {
            LaundryProduct laundryProduct = laundryOrderItem.getLaundryProduct();
            Integer result = laundryOrderItem.getLaundryProduct().getStock() + laundryOrderItem.getCount();
            laundryProduct.setStock(result);
            laundryProductRepository.save(laundryProduct);
        }
    }

    @Override
    public List<LaundryProduct> findByTypeAndCategoryAndStatus(Integer type, Integer category, Integer status) {
        return laundryProductRepository.findByTypeAndCategoryAndStatus(type,category,status);
    }

    @Override
    public Page<LaundryProduct> findByTypeAndCategoryAndStatus(Integer type, Integer category, Integer status, Pageable pageable) {
        return laundryProductRepository.findByTypeAndCategoryAndStatus(type,category,status,pageable);
    }

    @Override
    public synchronized Boolean judgeStock(LaundryOrder laundryOrder) {
        for (LaundryOrderItem laundryOrderItem : laundryOrder.getItems()) {
            LaundryProduct laundryProduct = findOne(laundryOrderItem.getLaundryProduct().getId());
            Integer result = laundryProduct.getStock() - laundryOrderItem.getCount();
            if (result < 0) {
               return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void decreaseStock(LaundryOrder laundryOrder) {
        for (LaundryOrderItem laundryOrderItem : laundryOrder.getItems()) {
            LaundryProduct laundryProduct = laundryOrderItem.getLaundryProduct();
            Integer result = laundryOrderItem.getLaundryProduct().getStock() - laundryOrderItem.getCount();
            if (result < 0) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
            }
            //todo  可以将所有item  遍历完后  统计将结果保存  saveAll   其他也是一样 2018年6月20日15:37:55
            laundryProduct.setStock(result);
            laundryProductRepository.save(laundryProduct);
        }
    }
}
