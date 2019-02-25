package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.OldOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/9/3 10:01
 */

@Repository
public interface OldOrderRepository extends JpaRepository<OldOrder,String> {

    Optional<OldOrder> findByNumber(String number);

    Page<OldOrder> findByUserId(String userId, Pageable pageable);

    List<OldOrder> findByUserId(String userId);



}
