package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.domain.RechargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/16 10:02
 */

@Repository
public interface RechargeOrderRepository extends JpaRepository<RechargeOrder,String> {

    List<RechargeOrder> findByCreatetimeBetweenAndPayStatus(Long startTime,Long endTime,Integer payStatus);
}
