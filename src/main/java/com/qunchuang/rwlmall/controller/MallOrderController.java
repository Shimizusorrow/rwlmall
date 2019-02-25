package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.MallOrderService;
import com.qunchuang.rwlmall.utils.ExportExcelUtil;
import com.qunchuang.rwlmall.utils.ListToPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/3/13 15:30
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/mallorder")
public class MallOrderController {

    @Autowired
    private MallOrderService mallOrderService;

    @Value("${excelpath}")
    String excelpath;


    /*根据商品id  查询订单*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("orderid") String orderId) {
        return mallOrderService.findOne(orderId);
    }

    /*派订单*/
    @RequestMapping("/delivery")
    @RoleAuthority(RoleAuthorityFunctionEnum.ORDER_MALL)
    public Object delivery(@RequestParam("orderid") String orderId, @RequestParam("storeid") String storeId, Principal principal) {
        mallOrderService.delivery(orderId, storeId, principal);
        return null;
    }

    /*根据商品编号 查询订单*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam("number") String number) {
        return mallOrderService.findByNumber(number);
    }

    /*创建订单*/
    @RequestMapping("/create")
    public Object create(@RequestBody MallOrder mallOrder, Principal principal) {
        return mallOrderService.create(mallOrder, principal.getName());
    }

    /*发货，运单上传*/
    @RequestMapping("/inbound")
    @RoleAuthority(RoleAuthorityFunctionEnum.ORDER_MALL)
    public Object photoUnload(@RequestParam("orderid") String orderId, @RequestParam("waybillimage") String waybillImage, Principal principal) {
        mallOrderService.photoUnload(orderId, waybillImage, principal);
        return null;
    }

    /*完结订单*/
    @RequestMapping("/finish")
    public Object finish(@RequestParam("orderid") String orderId) {
        return mallOrderService.finish(orderId);
    }

    /*作废订单*/
    @RequestMapping("/cancel")
    public Object cancel(@RequestParam("orderid") String orderId) {
        return mallOrderService.cancel(orderId);
    }


    /*查询所有订单  分页*/
    @RequestMapping("/findall")
    public Object findAll(@RequestParam(name = "size", required = false) Integer size,
                          @RequestParam(name = "page", required = false) Integer page) {
        if (size == null || page == null) {
            return mallOrderService.findAll();
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return mallOrderService.findAll(pageable);
    }

    /*根据用户的userid查询*/
    @RequestMapping("/findbyuserid")
    public Object findByUserId(@RequestParam("userid") String userId) {
        return mallOrderService.findByUserId(userId);
    }

    /*根据用户手机号查询*/
    @RequestMapping("/findbyphone")
    public Object findByPhone(@RequestParam("phone") String phone,
                              @RequestParam(name = "size", defaultValue = "8") Integer size,
                              @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return mallOrderService.findByPhone(phone, pageable);
    }

    /*根据订单状态查询*/
    @RequestMapping("/findbystatus")
    public Object findByStatus(@RequestParam("status") Integer status,
                               @RequestParam(name = "size", defaultValue = "8") Integer size,
                               @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return mallOrderService.findByStatus(status, pageable, principal);
    }


    /*查询指定时间内的订单*/
    @RequestMapping("/finbyordertime")
    public Object finByOrderTime(
            @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return mallOrderService.finByOrderTime(startTime, endTime, pageable);
    }

    /*查询指定区域状态订单*/
    @RequestMapping("/findbystatusandarea")
    public Object findByArea(@RequestParam("status") Integer status,
                             @RequestParam(name = "province", defaultValue = "") String province,
                             @RequestParam(name = "city", defaultValue = "") String city,
                             @RequestParam(name = "area", defaultValue = "") String area,
                             @RequestParam(name = "size", defaultValue = "8") Integer size,
                             @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return mallOrderService.findByStatusAndArea(status, province, city, area, pageable, principal);
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
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return mallOrderService.findByTimeAndArea(startTime, endTime, province, city, area, pageable);
    }

    /*商城订单按状态导出*/
    @RequestMapping("/export")
    @RoleAuthority(RoleAuthorityFunctionEnum.ORDER_MALL)
    protected Object export(HttpServletResponse response, @RequestParam("status") Integer status, Principal principal) {
        ExportExcelUtil.returnFile(status, response, excelpath);

        return null;
    }

    /*衣物通过numberor 手机号查询*/
    @RequestMapping("/findbycode")
    public Object findByCode(@RequestParam("code") String code,
                             @RequestParam(name = "size", defaultValue = "8") Integer size,
                             @RequestParam(name = "page", defaultValue = "1") Integer page) {

        List<MallOrder> mallOrders = mallOrderService.findByCode(code);

        return ListToPageUtil.convert(mallOrders, page, size);
    }

    /*撤销派送操作*/
    @RequestMapping("/canceldispatch")
    public Object cancelDispatch(@RequestParam("orderid") String orderId) {
        return mallOrderService.cancelDispatch(orderId);
    }

}
