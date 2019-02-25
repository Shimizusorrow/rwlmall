package com.qunchuang.rwlmall.service;


import com.qunchuang.rwlmall.domain.FurnitureOrder;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface LaundryProductService extends ProductService<LaundryProduct> {


    /*查找一种所属下的所有商品  分页*/
    Page<LaundryProduct> findByType(Pageable pageable, Integer type);

    /*查找某种所属下的所有商品*/
    List<LaundryProduct> findByType(Integer type);

    /*查找某种所属下的某种类别商品*/
    List<LaundryProduct> findByTypeAndCategory(Integer type, Integer category);

    /*加库存*/
    void increaseStock(LaundryOrder laundryOrder);

    /*减库存*/
    void decreaseStock(LaundryOrder laundryOrder);

    /*库存判断*/
    Boolean judgeStock(LaundryOrder laundryOrder);

    /*查找某种所属下的某种类别的上架商品*/
    List<LaundryProduct> findByTypeAndCategoryAndStatus(Integer type, Integer category, Integer status);

    /*查找某种所属下的某种类别的上架商品 分页*/
    Page<LaundryProduct> findByTypeAndCategoryAndStatus(Integer type, Integer category, Integer status, Pageable pageable);


/*
    void delete(String productId);

    List<LaundryProduct> findByIdIn(Set<String> productidset);*/

}
