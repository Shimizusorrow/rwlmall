package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.MallOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/12 10:21
 */

@Repository
public interface LaundryOrderRepository extends OrderRepository<LaundryOrder> {

    Page<LaundryOrder> findByType(Integer type, Pageable pageable);

    Page<LaundryOrder> findByTypeAndStatus(Integer type, Integer status, Pageable pageable);

    Page<LaundryOrder> findByTypeAndStatusAndAgentId(Integer type, Integer status, String agentId, Pageable pageable);

    List<LaundryOrder> findByTimeOutAndType(Integer timeOut, Integer type);

    List<LaundryOrder> findByTypeAndStatus(Integer type, Integer status);

    List<LaundryOrder> findByPhone(String phone);

    Page<LaundryOrder> findByTypeAndStatusAndProvinceContainingAndCityContainingAndAreaContaining(Integer type, Integer status, String province, String city, String area, Pageable pageable);

    List<LaundryOrder> findByTypeAndStatusNotAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area, Integer payStatus);

    List<LaundryOrder> findByTypeAndStatusNotAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(Integer type, Integer status, String agentId,Long startTime, Long endTime, String province, String city, String area, Integer payStatus);

    Page<LaundryOrder> findByTypeAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer type, Long startTime, Long endTime, String province, String city, String area, Pageable pageable);

    List<LaundryOrder> findByStoreIdAndStatusAndType(String storeId, Integer status, Integer type);

    Integer countByTypeAndStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area);

    Integer countByTypeAndStatusAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer type, Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);

    List<LaundryOrder> findByStoreIdAndCreatetimeBetweenAndStatusAndType(String storeId, Long startTime, Long endTime, Integer status, Integer type);

    List<LaundryOrder> findByStoreIdAndFinishTimeBetweenAndStatusAndType(String storeId, Long startTime, Long endTime, Integer status, Integer type);

    List<LaundryOrder> findByTypeAndStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area);

    List<LaundryOrder> findByTypeAndStoreIdAndStatusAndCreatetimeBetween(Integer type, String storeId, Integer status, Long startTime, Long endTime);

//    @Query("select l from LaundryOrder l where id in (select li.parent from  LaundryOrderItem li where li.barCode=?1)")
//    LaundryOrder findByItemBarCode(String barCode);

}
