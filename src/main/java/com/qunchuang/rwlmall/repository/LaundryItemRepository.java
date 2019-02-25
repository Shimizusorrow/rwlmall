package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/7/3 8:37
 */
@Repository
public interface LaundryItemRepository extends JpaRepository<LaundryOrderItem,String>{

    List<LaundryOrderItem> findByBarCode(String barCode);
}
