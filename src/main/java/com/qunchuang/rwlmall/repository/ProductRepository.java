package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.LaundryProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/8 15:43
 */

@NoRepositoryBean
public interface ProductRepository<T> extends JpaRepository<T,String> {

    Optional<T> findByNumber(String number);

    List<T> findByStatus(Integer status);

    List<T> findByStatusAndDateBeforeAndAutoStatus(Integer status,Long date,boolean autoStatus);

}
