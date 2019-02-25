package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/8 15:52
 */
public interface OrderService<T> {

    /*查询所有订单  分页*/
    Page<T> findAll(Pageable pageable);

    /*查询所有订单*/
    List<T> findAll();

    /*查询单个订单*/
    T findOne(String orderId);

    /*根据商品编号 查询商品*/
    T findByNumber(String number);

    /*根据用户的userId查询*/
    List<T> findByUserId(String userId);

    /*根据用户手机号查询*/
    Page<T> findByPhone(String phone, Pageable pageable);

    /*根据订单状态查询*/
    Page<T> findByStatus(Integer status, Pageable pageable);

    /*查询指定时间内的订单*/
    Page<T> finByOrderTime(Long startTime, Long endTime, Pageable pageable);

    //查询指定区域订单
    Page<T> findByStatusAndArea(Integer status, String province, String city, String area, Pageable pageable, Principal principal);

    /*按下单时间和付款状态查询*/
    List<T> findByTimeAndPayStatusAndStatus(Long startTime, Long endTime, Integer payStatus, Integer status);

    /*按时间 支付状态、支付方式查询*/
    List<T> findByTimeAndPayStatusAndPayMode(Long startTime, Long endTime, Integer payStatus, Integer payMode);

}
