package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Feedback;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.repository.FeedbackRepository;
import com.qunchuang.rwlmall.service.FeedbackService;
import com.qunchuang.rwlmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Curtain
 * @date 2018/4/25 10:12
 */

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<Feedback> findByCreatetimeBetween(Long startTime, Long endTime, Pageable pageable) {
        return feedbackRepository.findByCreatetimeBetween(startTime,endTime,pageable);
    }

    @Override
    public Feedback save(String  content,String phone, String userId) {

        Feedback feedback = new Feedback();

        User user = userService.findOne(userId);

        feedback.setName(user.getName());
        feedback.setMemberId(user.getNumber());
        feedback.setContent(content);
        feedback.setPhone(phone);

        return feedbackRepository.save(feedback);
    }
}
