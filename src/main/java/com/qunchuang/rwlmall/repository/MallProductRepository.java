package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.domain.MallProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/12 10:19
 */

@Repository
public interface MallProductRepository extends ProductRepository<MallProduct> {

    List<MallProduct> findByCategory(Integer category);

    List<MallProduct> findByCategoryAndStatus(Integer category,Integer status);

    Page<MallProduct> findByCategoryAndStatus(Integer category, Integer status,Pageable pageable);
}
