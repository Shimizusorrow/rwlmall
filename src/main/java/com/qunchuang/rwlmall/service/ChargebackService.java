package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Chargeback;
import com.qunchuang.rwlmall.domain.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/11/14 8:26
 */
public interface ChargebackService {

    /*添加*/
    Chargeback save(Chargeback chargeback, Principal principal);

    /*查找所有*/
    Page<Chargeback> findAll(Pageable pageable);

    /*按手机号查找*/
    Page<Chargeback> findByUserPhone(String userPhone,Pageable pageable);

    /*通过id查找一条*/
    Chargeback findOne(String chargeId);

    /*通过时间查询*/
    List<Chargeback> findByTime(Long startTime, Long endTime);

}
