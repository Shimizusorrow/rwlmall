package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.FurnitureOrder;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.MallOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/12 10:21
 */

@Repository
public interface MallOrderRepository extends OrderRepository<MallOrder> {

    List<MallOrder> findByStatusNotAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(Integer status, Long startTime, Long endTime, String province, String city, String area, Integer payStatus);

    List<MallOrder> findByStatusNotAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(Integer status,String agentId, Long startTime, Long endTime, String province, String city, String area, Integer payStatus);


    Page<MallOrder> findByCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Long startTime, Long endTime, String province, String city, String area, Pageable pageable);

    Integer countByStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer status, Long startTime, Long endTime, String province, String city, String area);

    Integer countByStatusAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);

    List<MallOrder> findByCreatetimeBetweenAndStatus(Long startTime, Long endTime, Integer status);

    List<MallOrder> findByStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer status, Long startTime, Long endTime, String province, String city, String area);

    List<MallOrder> findByStoreIdAndStatus(String storeId, Integer status);

    List<MallOrder> findByStatus(Integer status);

    Page<MallOrder> findByStatusAndAgentId(Integer status, String agentId, Pageable pageable);

    List<MallOrder> findByStoreIdAndCreatetimeBetweenAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    List<MallOrder> findByStoreIdAndFinishTimeBetweenAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    List<MallOrder> findByStoreIdAndStatusAndCreatetimeBetween(String storeId, Integer status, Long startTime, Long endtime);

    List<MallOrder> findByPhone(String phone);

}
