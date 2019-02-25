package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/17 10:47
 */

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    Page<Store> findByAgentId(String agentId, Pageable pageable);

    List<Store> findByProvinceContainingAndCityContainingAndAreaContaining(String province, String city, String area);

    List<Store> findByAgentIdAndProvinceContainingAndCityContainingAndAreaContaining(String agentId,String province, String city, String area);


    List<Store> findByStatusAndAccount(Integer status,boolean account);

    List<Store> findByAgentIdAndStatusAndAccount(String agentId, Integer status,boolean account);
}
