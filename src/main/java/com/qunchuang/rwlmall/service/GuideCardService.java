package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.GuideCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/24 14:11
 */
public interface GuideCardService {

    /*添加*/
    GuideCard save(GuideCard guideCard, Principal principal);

    /*查找所有*/
    Page<GuideCard> findAll(Pageable pageable);

    /*按手机号查找*/
    Page<GuideCard> findByUserPhone(String userPhone,Pageable pageable);

    /*通过id查找一条*/
    GuideCard findOne(String guideCardId);

}
