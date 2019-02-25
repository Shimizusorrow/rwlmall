package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/12 10:31
 */
public interface FurnitureOrderService extends OrderService<FurnitureOrder> {

    //创建订单
    FurnitureOrder create(FurnitureOrder furnitureOrder, String userId);

    /*派订单*/
    void delivery(String orderId, String storeId,Principal principal);

    //完结订单
    FurnitureOrder finish(String orderId);

    //取消订单
    FurnitureOrder cancel(String orderId);

    //支付订单
    FurnitureOrder paid(String orderId, Integer payMode);

    /*按状态查找*/
    List<FurnitureOrder> findByStatus(Integer status);

    /*按状态查找*/
    Page<FurnitureOrder> findByStatus(Integer status, Pageable pageable, Principal principal);

    /*代理商按状态查找*/
    Page<FurnitureOrder> findByStatusAndAgentId(Integer status, String agentId, Pageable pageable);

    //查找门店所属订单
    List<FurnitureOrder> findByStoreAndStatus(String storeId, Integer status);

    /*按时间和区域查找所有订单*/
    List<FurnitureOrder> findByStatusNotAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area);

    /*代理商 按时间和区域查找所有订单*/
    List<FurnitureOrder> findByStatusNotAndAgentIdAndTimeAndArea(Integer status,String agentId, Long startTime, Long endTime, String province, String city, String area);


    /*按时间和区域查找所有订单 分页*/
    Page<FurnitureOrder> findByTimeAndArea(Long startTime, Long endTime, String province, String city, String area, Pageable pageable);

    /*按时间区域查找未处理订单件数*/
    Integer countByStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area);

    /*代理商按时间区域查找未处理订单件数*/
    Integer countByStatusAndAgentIdAndTimeAndArea(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);


    /*按门店和时间查找订单*/
    List<FurnitureOrder> findByStoreIdAndTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    /*按门店和结束时间查找订单*/
    List<FurnitureOrder> findByStoreIdAndFinishTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    /*门店拍照上传*/
    void photoUnload(String orderId, List<FurnitureOrderItem> furnitureOrderItemList,Principal principal);

    /*按门店 订单支付状态和时间查找订单*/
    List<FurnitureOrder> findByStoreAndStatusAndTime(String storeId, Integer status, Long startTime, Long endTime);

    /*撤销派送操作*/
    FurnitureOrder cancelDispatch(String orderId);

    /*保存*/
    FurnitureOrder save(FurnitureOrder furnitureOrder);

    /*通过手机号或者number查询*/
    List<FurnitureOrder> findByCode(String code);

}
