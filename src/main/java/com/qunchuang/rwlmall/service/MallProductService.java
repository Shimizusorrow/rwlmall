package com.qunchuang.rwlmall.service;


import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.domain.MallOrderItem;
import com.qunchuang.rwlmall.domain.MallProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface MallProductService extends ProductService<MallProduct> {


    /*根据商品状态 是否上架  查询产品*/
    List<MallProduct> findByStatus(Integer status);

    /*查找某种类别商品*/
    List<MallProduct> findByCategory(Integer category);

    /*查找某类商品 同时满足 上架状态*/
    List<MallProduct> findByCategoryAndStatus(Integer category, Integer status);

    /*加库存*/
    void increaseStock(MallOrder mallOrder);

    /*减库存*/
    void decreaseStock(MallOrder mallOrder);

    /*库存判断*/
    Boolean judgeStock(MallOrder mallOrder);

    /*查找所有*/
    List<MallProduct> findAll();

    /*查找某类商品 同时满足 上架状态 分页*/
    Page<MallProduct> findByCategoryAndStatus(Integer category, Integer status, Pageable pageable);
/*
    void delete(String productId);

    List<LaundryProduct> findByIdIn(Set<String> productidset);*/

}
