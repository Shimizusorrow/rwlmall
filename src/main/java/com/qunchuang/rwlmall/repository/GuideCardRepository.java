package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.GuideCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Curtain
 * @date 2018/4/24 14:10
 */

@Repository
public interface GuideCardRepository extends JpaRepository<GuideCard,String> {


    Page<GuideCard> findByUserPhone(String userPhone,Pageable pageable);

}
