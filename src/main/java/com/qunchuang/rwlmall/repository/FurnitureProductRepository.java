package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.FurnitureProduct;
import com.qunchuang.rwlmall.domain.LaundryProduct;
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
public interface FurnitureProductRepository extends ProductRepository<FurnitureProduct> {

    Page<FurnitureProduct> findByStatus(Integer status,Pageable pageable);
}
