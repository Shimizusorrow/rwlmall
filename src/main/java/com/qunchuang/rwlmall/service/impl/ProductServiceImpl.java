package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.ProductRepository;
import com.qunchuang.rwlmall.service.ProductService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/4/8 16:36
 */
public class ProductServiceImpl<T> implements ProductService<T> {

    @Autowired
    private ProductRepository<T> productRepository;

    @Override
    public T findOne(String productId) {
        Optional<T> optional = productRepository.findById(Optional.ofNullable(productId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public T findByNumber(String number) {
        Optional<T> optional = productRepository.findByNumber(Optional.ofNullable(number).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public void save(List<T> product) {
        productRepository.saveAll(product);
    }

    @Override
    public T save(T product) {
        if (!Optional.ofNullable(product).isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_INFO_NOT_TRUE);
        }
        return productRepository.save(product);
    }

    @Override
    public T update(String productId, T product) {
        T result = findOne(productId);
        BeanUtils.copyProperties(product, result, BeanCopyUtil.getNullPropertyNames(product));
        return productRepository.save(result);
    }


    @Override
    public List<T> findByStatusAndDateBefore(Integer status, Long date) {
        return productRepository.findByStatusAndDateBeforeAndAutoStatus(status,date,true);
    }

    @Override
    public void upOffShelf(String productId) {
        T product = findOne(productId);

        Product result = (Product) product;
        if (result.getStatus().equals(ProductStatusEnum.UP.getCode())) {
            //上架改为下架
            result.setStatus(ProductStatusEnum.DOWN.getCode());
        } else if (result.getStatus().equals(ProductStatusEnum.DOWN.getCode())) {
            //下架改为上架
            result.setStatus(ProductStatusEnum.UP.getCode());

        } else {
           //不等于上架 也不等于下架  再直接改为下架
            result.setStatus(ProductStatusEnum.DOWN.getCode());
        }
        productRepository.save((T) result);

    }

}
