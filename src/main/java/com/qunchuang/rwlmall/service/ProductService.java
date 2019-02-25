package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.MallOrderItem;

import java.util.List;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/4/8 16:30
 */
public interface ProductService<T> {

    /*根据商品id  查询商品*/
    T findOne(String productId);

    /*根据商品编号 查询商品*/
    T findByNumber(String number);

    /*保存商品*/
    T save(T product);

    /*保存商品*/
    void save(List<T> product);

    /*修改商品*/
    T update(String productId, T product);

    /*上下架商品*/
    void upOffShelf(String productId);



    /*按时间和上架状态查询*/
    List<T> findByStatusAndDateBefore(Integer status,Long date);

}
