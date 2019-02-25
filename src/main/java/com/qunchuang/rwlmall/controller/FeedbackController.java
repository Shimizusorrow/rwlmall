package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.domain.Feedback;
import com.qunchuang.rwlmall.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/25 10:15
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/save")
    public Object save(@RequestParam("content") String content, @RequestParam("phone") String phone, Principal principal) {
            return feedbackService.save(content,phone, principal.getName());
    }

    @RequestMapping("/findbytime")
    public Object findByTime(
            @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Sort sort = new Sort(Sort.Order.desc("createtime"));
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        return feedbackService.findByCreatetimeBetween(startTime, endTime, pageable);
    }
}
