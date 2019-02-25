package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.MallOrder;
import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/8 15:38
 */

@NoRepositoryBean
public interface OrderRepository<T> extends JpaRepository<T,String>{

    Page<T> findByPhoneContaining(String phone, Pageable pageable);

    List<T> findByUserId(String userId);

    Page<T> findByStatus(Integer status, Pageable pageable);

    Page<T> findByCreatetimeBetween(Long startTime, Long endTime, Pageable pageable);

    Optional<T> findByNumber(String number);

    Page<T> findByStatusAndProvinceContainingAndCityContainingAndAreaContaining(Integer status,String province, String city, String area, Pageable pageable);

    List<T> findByCreatetimeBetweenAndPayStatusAndStatus(Long startTime,Long endTime,Integer payStatus,Integer status);

    List<T> findByCreatetimeBetweenAndPayStatusAndPayMode(Long startTime,Long endTime,Integer payStatus,Integer payMode);

}
