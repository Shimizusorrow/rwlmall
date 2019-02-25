package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/25 14:28
 */
public interface RefundService {

    /*添加*/
    Refund save(Refund refund, Principal principal);

    /*查找所有*/
    Page<Refund> findAll(Pageable pageable);

    /*按手机号查找*/
    Page<Refund> findByUserPhone(String userPhone,Pageable pageable);

    /*通过id查找一条*/
    Refund findOne(String refundId);

    /*通过时间查询*/
    List<Refund> findByTime(Long startTime,Long endTime);

}
