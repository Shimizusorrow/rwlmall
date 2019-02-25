package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.MallProductRepository;
import com.qunchuang.rwlmall.service.MallProductService;
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
public class MallProductServiceImpl extends ProductServiceImpl<MallProduct> implements MallProductService {

    @Autowired
    private MallProductRepository mallProductRepository;



    @Override
    public List<MallProduct> findByStatus(Integer status) {
        List<MallProduct> list = mallProductRepository.findByStatus(status);
        return list;
    }

    @Override
    public List<MallProduct> findByCategoryAndStatus(Integer category, Integer status) {

        return mallProductRepository.findByCategoryAndStatus(category, status);
    }

    @Override
    public List<MallProduct> findByCategory(Integer category) {
        return mallProductRepository.findByCategory(category);
    }


    @Override
    public Page<MallProduct> findByCategoryAndStatus(Integer category, Integer status, Pageable pageable) {
        return mallProductRepository.findByCategoryAndStatus(category,status,pageable);
    }

    @Override
    public List<MallProduct> findAll() {
        return mallProductRepository.findAll();
    }

    @Override
    public synchronized Boolean judgeStock(MallOrder mallOrder) {
        for (MallOrderItem mallOrderItem : mallOrder.getItems()) {
            MallProduct mallProduct = findOne(mallOrderItem.getMallProduct().getId());
            Integer result = mallProduct.getStock() - mallOrderItem.getCount();
            if (result < 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void decreaseStock(MallOrder mallOrder) {
        for (MallOrderItem mallOrderItem : mallOrder.getItems()) {
            MallProduct mallProduct = mallOrderItem.getMallProduct();
            Integer result = mallOrderItem.getMallProduct().getStock() - mallOrderItem.getCount();
            if (result < 0) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
            }
            mallProduct.setStock(result);
            mallProductRepository.save(mallProduct);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(MallOrder mallOrder) {
        for (MallOrderItem mallOrderItem : mallOrder.getItems()) {
            MallProduct mallProduct = mallOrderItem.getMallProduct();
            Integer result = mallOrderItem.getMallProduct().getStock() + mallOrderItem.getCount();
            mallProduct.setStock(result);
            mallProductRepository.save(mallProduct);
        }
    }
}
