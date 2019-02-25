package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.bean.MyPage;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.enums.LaundryOrderTimeEnum;
import com.qunchuang.rwlmall.service.LaundryOrderService;
import com.qunchuang.rwlmall.utils.ListToPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/13 15:30
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/laundryorder")
public class LaundryOrderController {

    @Autowired
    private LaundryOrderService laundryOrderService;

    /*根据商品id  查询订单*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("orderid") String orderId) {

        return laundryOrderService.findOne(orderId);
    }

    /*根据商品编号 查询订单*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam("number") String number) {
        return laundryOrderService.findByNumber(number);
    }

    /*创建订单*/
    @RequestMapping("/create")
    public Object create(@RequestBody LaundryOrder laundryOrder, Principal principal) {
        return laundryOrderService.create(laundryOrder, principal.getName());
    }

//    /*派订单*/
//    @RequestMapping("/delivery")
//    public Object delivery(@RequestParam("orderid") String orderId) {
//        return laundryOrderService.delivery(orderId);
//    }
//
//    /*收订单*/
//    @RequestMapping("/received")
//    public Object received(@RequestParam("orderid") String orderId) {
//        return laundryOrderService.received(orderId);
//    }


    /*入站备注*/
    @RequestMapping("inboundremark")
    public Object inboundRemark(@RequestParam("orderid") String orderId, @RequestParam("remark") String remark, Principal principal) {
        return laundryOrderService.inboundRemark(orderId, remark, principal);
    }

//    /*入站*/
//    @RequestMapping("/inbound")
//    public Object inbound(@RequestParam("orderid") String orderId) {
//        return laundryOrderService.inbound(orderId);
//    }

    /*通过衣物条形码查询*/
    @RequestMapping("/findbyitembarcode")
    public Object findByItemBarCode(@RequestParam("barcode") String barCode, Principal principal) {
        return laundryOrderService.findByItemBarCode(barCode, principal);
    }


    /*上挂*/
    @RequestMapping("/hangon")
    public Object hangOn(@RequestParam("orderid") String orderId, Principal principal) {
        return laundryOrderService.hangOn(orderId, principal);
    }

    /*完结订单*/
    @RequestMapping("/finish")
    public Object finish(@RequestParam("orderid") String orderId) {
        return laundryOrderService.finish(orderId);
    }

    /*作废订单*/
    @RequestMapping("/cancel")
    public Object cancel(@RequestParam("orderid") String orderId) {
        return laundryOrderService.cancel(orderId);
    }

    /*查询所有订单  分页*/
    @RequestMapping("/findall")
    public Object findAll(@RequestParam(name = "size", required = false) Integer size,
                          @RequestParam(name = "page", required = false) Integer page) {
        if (size == null || page == null) {
            return laundryOrderService.findAll();
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return laundryOrderService.findAll(pageable);
    }

    /*根据用户的userid查询*/
    @RequestMapping("/findbyuserid")
    public Object findByUserId(@RequestParam(value = "userid", defaultValue = "") String userId, Principal principal) {
        if ("".equals(userId)) {
            return laundryOrderService.findByUserId(principal.getName());
        }
        return laundryOrderService.findByUserId(userId);
    }

    /*根据用户手机号查询*/
    @RequestMapping("/findbyphone")
    public Object findByPhone(@RequestParam("phone") String phone,
                              @RequestParam(name = "size", defaultValue = "8") Integer size,
                              @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return laundryOrderService.findByPhone(phone, pageable);
    }

    /*根据商品的所属查询订单*/
    @RequestMapping("/findbytype")
    public Object findByType(@RequestParam("type") Integer type,
                             @RequestParam(name = "size", defaultValue = "8") Integer size,
                             @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return laundryOrderService.findByType(type, pageable);
    }

    /*根据商品的所属 and 订单状态 查询订单*/
    @RequestMapping("/findbytypeandstatus")
    public Object findByTypeAndStatus(@RequestParam("type") Integer type,
                                      @RequestParam("status") Integer status,
                                      @RequestParam(name = "size", defaultValue = "8") Integer size,
                                      @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return laundryOrderService.findByTypeAndStatus(type, status, pageable, principal);
    }

    /*查询指定时间内的订单*/
    @RequestMapping("/finbyordertime")
    public Object finByOrderTime(
            @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return laundryOrderService.finByOrderTime(startTime, endTime, pageable);
    }


    /*查找超时订单*/
    @RequestMapping("/findbytimeoutandtype")
    public Object findByTimeOut(Integer type,@RequestParam(name = "size", defaultValue = "8") Integer size,
                                @RequestParam(name = "page", defaultValue = "1") Integer page) {
        List<LaundryOrder> laundryOrders = laundryOrderService.findByTimeOutAndType(LaundryOrderTimeEnum.TIME_OUT.getCode(), type);
        return ListToPageUtil.convert(laundryOrders, page, size);
    }

    /*查询指定区域状态订单*/
    @RequestMapping("/findbytypeandstatusandarea")
    public Object findByArea(@RequestParam("type") Integer type,
                             @RequestParam("status") Integer status,
                             @RequestParam(name = "province", defaultValue = "") String province,
                             @RequestParam(name = "city", defaultValue = "") String city,
                             @RequestParam(name = "area", defaultValue = "") String area,
                             @RequestParam(name = "size", defaultValue = "8") Integer size,
                             @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return laundryOrderService.findByTypeAndStatusAndArea(type, status, province, city, area, pageable, principal);
    }

    /*查找门店 + 状态  所属订单*/
    @RequestMapping("/findbystoreandstatusandtype")
    public Object findByStoreAndStatusAndType(@RequestParam("status") Integer status,
                                              @RequestParam("type") Integer type, Principal principal) {
        Role role = (Role) principal;
        return laundryOrderService.findByStoreAndStatusAndType(role.getAccountId(), status, type);
    }


    /*派给门店*/
    @RequestMapping("/storepickup")
    public Object storePickup(@RequestParam("orderid") String orderId, @RequestParam("storeid") String storeId, Principal principal) {
        laundryOrderService.storePickup(orderId, storeId, principal);
        return null;
    }

    /*门店送单*/
    @RequestMapping("/storegiveback")
    public Object storeGiveBack(@RequestParam("orderid") String orderId, @RequestParam("storeid") String storeId, Principal principal) {
        laundryOrderService.storeGiveBack(orderId, storeId, principal);
        return null;
    }

    /*派给顺丰*/
    @RequestMapping("/sfcollect")
    public Object sfCollect(@RequestParam("orderid") String orderId, @RequestParam("storeid") String storeId) {
        laundryOrderService.sfCollect(orderId, storeId);
        return null;
    }

    /*顺丰寄还*/
    @RequestMapping("/sfsend")
    public Object sfSend(@RequestParam("orderid") String orderId, @RequestParam("storeid") String storeId) {
        laundryOrderService.sfSend(orderId, storeId);
        return null;
    }

    /*撤销派送操作*/
    @RequestMapping("/canceldispatch")
    public Object cancelDispatch(@RequestParam("orderid") String orderId) {
        return laundryOrderService.cancelDispatch(orderId);
    }


    /*按时间区域 查找订单*/
    @RequestMapping("/findbytypeandtimeandarea")
    public Object findByTypeAndTimeAndArea(@RequestParam("type") Integer type,
                                           @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                           @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                           @RequestParam(name = "province", defaultValue = "") String province,
                                           @RequestParam(name = "city", defaultValue = "") String city,
                                           @RequestParam(name = "area", defaultValue = "") String area,
                                           @RequestParam(name = "size", defaultValue = "8") Integer size,
                                           @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return laundryOrderService.findByTypeAndTimeAndArea(type, startTime, endTime, province, city, area, pageable);
    }

    /*门店操作  衣物入站*/
    @RequestMapping("/singletoninbound")
    public Object singletonInbound(@RequestParam("orderid") String orderId, @RequestBody List<LaundryOrderItem> laundryOrderItemList, Principal principal) {

        laundryOrderService.singletonInbound(orderId, laundryOrderItemList, principal);
        return null;
    }

    /*门店操作  条形码去重*/
    @RequestMapping("/distinctbarcode")
    public Object distinctBarCode(@RequestParam("barcode") String barCode) {
        return laundryOrderService.distinctBarCode(barCode);
    }

    /*门店操作  衣物上挂*/
    @RequestMapping("/singletonhangon")
    public Object singletonHangOn(@RequestParam("orderid") String orderId, @RequestParam("orderitemid") String orderItemId, Principal principal) {
        laundryOrderService.singletonHangOn(orderId, orderItemId, principal);
        return null;
    }

    /*衣物通过number or 条形码 or 手机号查询*/
    @RequestMapping("/findbytypeandcode")
    public Object findByTypeAndCode(@RequestParam("type") Integer type, @RequestParam("code") String code,
                                    @RequestParam(name = "size", defaultValue = "8") Integer size,
                                    @RequestParam(name = "page", defaultValue = "1") Integer page) {

        List<LaundryOrder> laundryOrders = laundryOrderService.findByTypeAndCode(type, code);

        return ListToPageUtil.convert(laundryOrders, page, size);

    }
}
