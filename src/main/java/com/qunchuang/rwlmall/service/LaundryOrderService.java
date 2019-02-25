package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.FurnitureOrder;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import com.qunchuang.rwlmall.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.print.attribute.standard.PrinterName;
import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/12 10:31
 */
public interface LaundryOrderService extends OrderService<LaundryOrder> {

    //创建订单
    LaundryOrder create(LaundryOrder laundryOrder, String userId);

    /*门店自取*/
    void storePickup(String orderId, String storeId, Principal principal);

    /*顺丰收单*/
    void sfCollect(String orderId, String storeId);

    /*顺丰寄还*/
    void sfSend(String orderId,String storeId);

    /*门店送还*/
    void storeGiveBack(String orderId, String storeId, Principal principal);

    /*派订单*/
    void delivery(LaundryOrder laundryOrder);

    /*订单送还*/
    void giveBack(LaundryOrder laundryOrder);

//    /*收订单*/
//    LaundryOrder received(String orderId);

    /*入站*/
    LaundryOrder inbound(String orderId);

    /*入站备注*/
    LaundryOrder inboundRemark(String orderId, String remark, Principal principal);

    /*上挂*/
    LaundryOrder hangOn(String orderId, Principal principal);

    //完结订单
    LaundryOrder finish(String orderId);

    /*订单超时*/
    LaundryOrder timeOut(String orderId);

    //取消订单
    LaundryOrder cancel(String orderId);

    //支付订单
    LaundryOrder paid(String orderId, Integer payMode);

    /*查找超时订单*/
    List<LaundryOrder> findByTimeOutAndType(Integer timeOut, Integer type);

    /*查找洗衣  或者高端洗护 所有订单*/
    Page<LaundryOrder> findByType(Integer type, Pageable pageable);

    /*查找洗衣或者高端洗护下的  某种状态的订单*/
    Page<LaundryOrder> findByTypeAndStatus(Integer type, Integer status, Pageable pageable, Principal principal);

    /*代理商 查找洗衣或者高端洗护下的  某种状态的订单*/
    Page<LaundryOrder> findByTypeAndStatusAndAgentId(Integer type, Integer status, String agentId, Pageable pageable);

    /*商品入站*/
    void singletonInbound(String orderId, List<LaundryOrderItem> laundryOrderItemList, Principal principal);

    /*单件商品上挂*/
    void singletonHangOn(String orderId, String orderItemId, Principal principal);

    /*订单按区域类型状态查询*/
    Page<LaundryOrder> findByTypeAndStatusAndArea(Integer type, Integer status, String province, String city, String area, Pageable pageable, Principal principal);

    //查找门店所属订单
    List<LaundryOrder> findByStoreAndStatusAndType(String storeId, Integer status, Integer type);

    /*按状态+类型查找订单*/
    List<LaundryOrder> findByTypeAndStatus(Integer type, Integer status);

    /*保存*/
    void saveAll(List<LaundryOrder> list);

    /*按时间和区域查找所有订单*/
    List<LaundryOrder> findByTypeAndStatusNotAndTimeAndArea(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area);

    /*代理商按时间和区域查找所有订单*/
    List<LaundryOrder> findByTypeAndStatusNotAndAgentIdAndTimeAndArea(Integer type, Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);

    /*按时间和区域查找所有订单 分页*/
    Page<LaundryOrder> findByTypeAndTimeAndArea(Integer type, Long startTime, Long endTime, String province, String city, String area, Pageable pageable);

    /*按时间区域查找未处理订单件数*/
    Integer countByTypeAndStatusAndTimeAndArea(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area);

    /*代理商按时间区域查找未处理订单件数*/
    Integer countByTypeAndStatusAndAgentIdAndTimeAndArea(Integer type, Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area);

    /*按门店和创建时间查找订单*/
    List<LaundryOrder> findByStoreIdAndTimeAndStatusAndType(String storeId, Long startTime, Long endTime, Integer status, Integer type);

    /*按门店和完结时间查找订单*/
    List<LaundryOrder> findByStoreIdAndFinishTimeAndStatusAndType(String storeId, Long startTime, Long endTime, Integer status, Integer type);


    /*按门店 订单支付状态和时间查找订单*/
    List<LaundryOrder> findByTypeAndStoreAndStatusAndTime(Integer type, String storeId, Integer status, Long startTime, Long endTime);

    /*通过订单中衣服条形码查询订单*/
    List<LaundryOrder> findByItemBarCode(String barCode, Principal principal);

    /*撤销派送操作*/
    LaundryOrder cancelDispatch(String orderId);

    /*保存*/
    LaundryOrder save(LaundryOrder laundryOrder);

    /*通过类型状态 手机号或者number或者衣物条形码查询*/
    List<LaundryOrder> findByTypeAndCode(Integer type, String code);

    /**
     * 去重条形码
     * @return
     */
    String distinctBarCode(String barCode);

}
