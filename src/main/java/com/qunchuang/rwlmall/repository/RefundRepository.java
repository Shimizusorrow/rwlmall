package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/25 14:21
 */
public interface RefundRepository extends JpaRepository<Refund,String>{

    Page<Refund> findByUserPhone(String userPhone, Pageable pageable);

    List<Refund> findByCreatetimeBetween(Long startTime,Long endTime);
}
