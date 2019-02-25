package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.FurnitureOrder;
import com.qunchuang.rwlmall.domain.FurnitureOrderItem;
import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.FurnitureOrderService;
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
@RequestMapping("/furnitureorder")
public class FurnitureOrderController {

    @Autowired
    private FurnitureOrderService furnitureOrderService;

    /*根据商品id  查询订单*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("orderid") String orderId) {

        return furnitureOrderService.findOne(orderId);
    }

    /*根据商品编号 查询订单*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam("number") String number) {
        return furnitureOrderService.findByNumber(number);
    }

    /*创建订单*/
    @RequestMapping("/create")
    public Object create(@RequestBody FurnitureOrder furnitureOrder, Principal principal) {
        return furnitureOrderService.create(furnitureOrder, principal.getName());
    }

    /*派订单*/
    @RequestMapping("/delivery")
    @RoleAuthority(RoleAuthorityFunctionEnum.ORDER_FURNITURE)
    public Object delivery(@RequestParam("orderid") String orderId, @RequestParam("storeid") String storeId,Principal principal) {
         furnitureOrderService.delivery(orderId, storeId,principal);
         return null;
    }

    /*拍照上传*/
    @RequestMapping("/inbound")
    @RoleAuthority(RoleAuthorityFunctionEnum.ORDER_FURNITURE)
    public Object photoUnload(@RequestParam("orderid") String orderId, @RequestBody List<FurnitureOrderItem> furnitureOrderItemList, Principal principal){
        furnitureOrderService.photoUnload(orderId,furnitureOrderItemList,principal);
        return null;
    }


    /*完结订单*/
    @RequestMapping("/finish")
    public Object finish(@RequestParam("orderid") String orderId) {
        return furnitureOrderService.finish(orderId);
    }

    /*作废订单*/
    @RequestMapping("/cancel")
    public Object cancel(@RequestParam("orderid") String orderId) {
        return furnitureOrderService.cancel(orderId);
    }

    /*查询所有订单  分页*/
    @RequestMapping("/findall")
    public Object findAll(@RequestParam(name = "size", required = false) Integer size,
                          @RequestParam(name = "page", required = false) Integer page) {
        if (size == null || page == null) {
            return furnitureOrderService.findAll();
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return furnitureOrderService.findAll(pageable);
    }

    /*根据用户的userid查询*/
    @RequestMapping("/findbyuserid")
    public Object findByUserId(@RequestParam("userid") String userId) {
        return furnitureOrderService.findByUserId(userId);
    }

    /*根据用户手机号查询*/
    @RequestMapping("/findbyphone")
    public Object findByPhone(@RequestParam("phone") String phone,
                              @RequestParam(name = "size", defaultValue = "8") Integer size,
                              @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return furnitureOrderService.findByPhone(phone, pageable);
    }

    /*根据订单状态查询*/
    @RequestMapping("/findbystatus")
    public Object findByStatus(@RequestParam("status") Integer status,
                               @RequestParam(name = "size", defaultValue = "8") Integer size,
                               @RequestParam(name = "page", defaultValue = "1") Integer page,Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return furnitureOrderService.findByStatus(status, pageable,principal);
    }

    /*查询指定时间内的订单*/
    @RequestMapping("/finbyordertime")
    public Object finByOrderTime(
            @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return furnitureOrderService.finByOrderTime(startTime, endTime, pageable);
    }

    /*查询指定区域状态订单*/
    @RequestMapping("/findbystatusandarea")
    public Object findByArea(@RequestParam("status") Integer status,
                             @RequestParam(name = "province", defaultValue = "") String province,
                             @RequestParam(name = "city", defaultValue = "") String city,
                             @RequestParam(name = "area", defaultValue = "") String area,
                             @RequestParam(name = "size", defaultValue = "8") Integer size,
                             @RequestParam(name = "page", defaultValue = "1") Integer page,Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return furnitureOrderService.findByStatusAndArea(status, province, city, area, pageable,principal);
    }

    /*撤销派送操作*/
    @RequestMapping("/canceldispatch")
    public Object cancelDispatch(@RequestParam("orderid") String orderId) {
        return furnitureOrderService.cancelDispatch(orderId);
    }


    /*门店 + 状态所属订单*/
    @RequestMapping("/findbystoreandstatus")
    public Object findByStoreAndStatus(@RequestParam("status") Integer status, Principal principal) {
        Role role = (Role) principal;
        return furnitureOrderService.findByStoreAndStatus(role.getAccountId(), status);
    }

    /*按时间区域 查找订单*/
    @RequestMapping("/findbytimeandarea")
    public Object findByTimeAndArea(
            @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
            @RequestParam(name = "province", defaultValue = "") String province,
            @RequestParam(name = "city", defaultValue = "") String city,
            @RequestParam(name = "area", defaultValue = "") String area,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return furnitureOrderService.findByTimeAndArea(startTime, endTime, province, city, area, pageable);
    }

    /*衣物通过numberor 手机号查询*/
    @RequestMapping("/findbycode")
    public Object findByCode(@RequestParam("code") String code,
                             @RequestParam(name = "size", defaultValue = "8") Integer size,
                             @RequestParam(name = "page", defaultValue = "1") Integer page) {

        List<FurnitureOrder> furnitureOrders = furnitureOrderService.findByCode(code);

        return ListToPageUtil.convert(furnitureOrders, page, size);

    }
}
