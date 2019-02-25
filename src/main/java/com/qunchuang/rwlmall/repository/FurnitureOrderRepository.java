package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.FurnitureOrder;
import com.qunchuang.rwlmall.domain.MallOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/12 10:21
 */

@Repository
public interface FurnitureOrderRepository extends OrderRepository<FurnitureOrder> {

    List<FurnitureOrder> findByStatus(Integer status);

    List<FurnitureOrder> findByStoreIdAndStatus(String storeId, Integer status);

    List<FurnitureOrder> findByStatusNotAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(Integer status, Long startTime, Long endTime, String province, String city, String area, Integer payStatus);

    List<FurnitureOrder> findByStatusNotAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(Integer status,String agentId, Long startTime, Long endTime, String province, String city, String area, Integer payStatus);

    Page<FurnitureOrder> findByCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Long startTime, Long endTime, String province, String city, String area, Pageable pageable);

    Integer countByStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer status, Long startTime, Long endTime, String province, String city, String area);

    Integer countByStatusAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);

    List<FurnitureOrder> findByStoreIdAndCreatetimeBetweenAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    List<FurnitureOrder> findByStoreIdAndFinishTimeBetweenAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    List<FurnitureOrder> findByStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer status, Long startTime, Long endTime, String province, String city, String area);

    Page<FurnitureOrder> findByStatusAndAgentId(Integer status, String agentId, Pageable pageable);

    List<FurnitureOrder> findByStoreIdAndStatusAndCreatetimeBetween(String storeId,Integer status,Long startTime,Long endtime);

    List<FurnitureOrder> findByPhone(String phone);



}
