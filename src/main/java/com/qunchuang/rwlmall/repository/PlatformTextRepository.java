package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.PlatformText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Curtain
 * @date 2018/4/28 14:46
 */
@Repository
public interface PlatformTextRepository extends JpaRepository<PlatformText,String>{
    
       PlatformText findByType(Integer type);

}
