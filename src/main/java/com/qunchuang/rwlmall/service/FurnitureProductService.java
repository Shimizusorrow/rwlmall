package com.qunchuang.rwlmall.service;


import com.qunchuang.rwlmall.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface FurnitureProductService extends ProductService<FurnitureProduct> {

    /*根据商品状态 是否上架  查询产品*/
    List<FurnitureProduct> findByStatus(Integer status);

    /*加库存*/
    void increaseStock(FurnitureOrder furnitureOrder);

    /*减库存*/
    void decreaseStock(FurnitureOrder furnitureOrder);

    /*查找所有*/
    List<FurnitureProduct> findAll();

    /*库存判断*/
    Boolean judgeStock(FurnitureOrder furnitureOrder);

    /*查找上架商品*/
    Page<FurnitureProduct> findByStatus(Integer status,Pageable pageable);


/*
    void delete(String productId);

    List<LaundryProduct> findByIdIn(Set<String> productidset);*/

}
