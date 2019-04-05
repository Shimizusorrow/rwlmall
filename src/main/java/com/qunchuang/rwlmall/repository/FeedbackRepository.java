package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Curtain
 * @date 2018/4/25 9:12
 */

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,String> {

    Page<Feedback> findByCreatetimeBetween(Long startTime, Long endTime, Pageable pageable);

}
