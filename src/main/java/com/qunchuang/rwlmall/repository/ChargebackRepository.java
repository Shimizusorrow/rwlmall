package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Chargeback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/11/14 8:22
 */

@Repository
public interface ChargebackRepository extends JpaRepository<Chargeback,String> {

    Page<Chargeback> findByUserPhone(String userPhone, Pageable pageable);

    List<Chargeback> findByCreatetimeBetween(Long startTime, Long endTime);
}

