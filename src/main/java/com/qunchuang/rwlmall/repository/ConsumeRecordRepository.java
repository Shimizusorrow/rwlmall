package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.ConsumeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/7/16 14:25
 */
public interface ConsumeRecordRepository extends JpaRepository<ConsumeRecord, String> {

    Page<ConsumeRecord> findByUserIdAndCreatetimeBetween(String userId, Long startTime, Long endTime, Pageable pageable);

    List<ConsumeRecord> findByUserIdAndCreatetimeBetween(String userId, Long startTime, Long endTime, Sort sort);
}
