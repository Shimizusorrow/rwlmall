package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.FurnitureOrder;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.domain.MallOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/12 10:31
 */
public interface MallOrderService extends OrderService<MallOrder> {

    //创建订单
    MallOrder create(MallOrder mallOrder, String userId);

    //分派订单
    void delivery(String orderId, String storeId, Principal principal);

    //完结订单
    MallOrder finish(String orderId);

    /*门店拍照上传（发订单）*/
    void photoUnload(String orderId, String waybillImage,Principal principal);

    //取消订单
    MallOrder cancel(String orderId);

    //支付订单
    MallOrder paid(String orderId, Integer payMode);

    /*按时间和区域查找所有已支付的订单 */
    List<MallOrder> findByStatusNotAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area);

    /*代理商按时间和区域查找所有已支付的订单 */
    List<MallOrder> findByStatusNotAndAgentIdAndTimeAndArea(Integer status,String agentId, Long startTime, Long endTime, String province, String city, String area);


    /*按时间和区域查找所有订单 分页*/
    Page<MallOrder> findByTimeAndArea(Long startTime, Long endTime, String province, String city, String area, Pageable pageable);

    /*按时间区域查找未处理订单件数*/
    Integer countByStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area);

    /*代理商按时间区域查找未处理订单件数*/
    Integer countByStatusAndAgentIdAndTimeAndArea(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);

    /*按状态和时间查找订单*/
    List<MallOrder> findByTimeAndStatus(Long startTime, Long endTime, Integer status);

    /*按门店和时间查找订单*/
    List<MallOrder> findByStoreIdAndTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    /*按门店和结束时间查找订单*/
    List<MallOrder> findByStoreIdAndFinishTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status);

    /*按状态查询订单*/
    List<MallOrder> findByStatus(Integer status);

    /*按状态查找订单*/
    Page<MallOrder> findByStatus(Integer status, Pageable pageable, Principal principal);

    /*代理商按状态查找订单*/
    Page<MallOrder> findByStatusAndAgentId(Integer status, String agentId, Pageable pageable);

    //查找门店所属订单
    List<MallOrder> findByStoreAndStatus(String storeId, Integer status);

    /*按门店 订单支付状态和时间查找订单*/
    List<MallOrder> findByStoreAndStatusAndTime(String storeId, Integer status, Long startTime, Long endTime);

    /*保存*/
    MallOrder save(MallOrder mallOrder);

    /*通过手机号或者number查询*/
    List<MallOrder> findByCode(String code);

    /*撤销派送操作*/
    MallOrder cancelDispatch(String orderId);

}
