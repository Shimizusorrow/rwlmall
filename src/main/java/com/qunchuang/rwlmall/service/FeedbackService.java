package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Curtain
 * @date 2018/4/25 9:15
 */
public interface FeedbackService {

    /*按时间查询*/
    Page<Feedback> findByCreatetimeBetween(Long startTime, Long endTime, Pageable pageable);

    /*保存一条反馈*/
    Feedback save(String content,String phone,String userId);

}
