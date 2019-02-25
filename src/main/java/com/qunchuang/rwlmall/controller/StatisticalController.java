package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.enums.OrderTypeEnum;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/23 17:05
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/statistical")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    /*订单分析*/
    @RequestMapping("/orderanalysis")
    @RoleAuthority(RoleAuthorityFunctionEnum.ORDER_ANALYSE)
    public Object orderAnalysis(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                @RequestParam(name = "province", defaultValue = "") String province,
                                @RequestParam(name = "city", defaultValue = "") String city,
                                @RequestParam(name = "area", defaultValue = "") String area, Principal principal) {
        return statisticalService.orderAnalysis(startTime, endTime, province, city, area, principal);
    }

    /*新订单数量统计*/
    @RequestMapping("/newordercount")
    public Object newOrderCount(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                @RequestParam(name = "province", defaultValue = "") String province,
                                @RequestParam(name = "city", defaultValue = "") String city,
                                @RequestParam(name = "area", defaultValue = "") String area, Principal principal) {
        return statisticalService.newOrderCount(startTime, endTime, province, city, area, principal);
    }

    /*获取系统时间*/
    @RequestMapping("/getcurrenttime")
    public Object getCurrentTime() {
        return System.currentTimeMillis();
    }


    //按照用户消费次数 地区  时间 分别统计用户人数
    @RequestMapping("/usercountstatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.USER_STATISTICAL)
    public Object userCountStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                       @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                       @RequestParam(name = "country", defaultValue = "") String country,
                                       @RequestParam(name = "province", defaultValue = "") String province,
                                       @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
/*
        //去掉city参数中的市
        if (city.endsWith("市")){
            city = city.substring(0,city.length()-1);
        }
*/

        return statisticalService.userCountStatistical(startTime, endTime, country, province, city);
    }

    /*门店查找所属新订单*/
    @RequestMapping("/storefindneworder")
    public Object storeFindNewOrder(String key, Integer type, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return statisticalService.storeFindNewOrder(role.getAccountId(), key, type);
    }


    /*门店所属新订单*/
    @RequestMapping("/allinboundstoreorder")
    public Object allInboundStoreOrder(@RequestParam("storeid") String storeId) {
        return statisticalService.allInboundStoreOrder(storeId);
    }

    /*门店所属已入站订单*/
    @RequestMapping("/allhangonstoreorder")
    public Object allHangOnStoreOrder(@RequestParam("storeid") String storeId) {
        return statisticalService.allHangOnStoreOrder(storeId);
    }


    /*门店所属的所有未完结订单*/
    @RequestMapping("/allstoreorder")
    public Object allStoreOrder(@RequestParam("storeid") String storeId, Integer type) {
        return statisticalService.allStoreOrder(storeId, type);
    }


    /*商户结算*/
    @RequestMapping("/storestatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.STORE_STATISTICAL)
    public Object storeStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                   @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                   @RequestParam(name = "area", defaultValue = "") String area,
                                   @RequestParam(name = "province", defaultValue = "") String province,
                                   @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.storeAnalysis(startTime, endTime, province, city, area, principal);
    }

    /*商户结算每日详情*/
    @RequestMapping("/storedayanalysis")
    @RoleAuthority(RoleAuthorityFunctionEnum.STORE_STATISTICAL)
    public Object storeDayAnalysis(@RequestParam("storeid") String storeId, @RequestParam(name = "page", defaultValue = "1") Integer page) {
        return statisticalService.storeDayAnalysis(storeId, page - 1);
    }

    /*会员消费统计_洗衣*/
    @RequestMapping("/laundryconsumestatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object laundryConsumeStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                            @RequestParam(name = "area", defaultValue = "") String area,
                                            @RequestParam(name = "province", defaultValue = "") String province,
                                            @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.laundryConsumeStatistical(startTime, endTime, province, city, area, OrderTypeEnum.LAUNDRY.getCode());
    }

    /*会员消费统计_高端洗护*/
    @RequestMapping("/highlaundryconsumestatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object highLaundryConsumeStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                                @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                                @RequestParam(name = "area", defaultValue = "") String area,
                                                @RequestParam(name = "province", defaultValue = "") String province,
                                                @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.laundryConsumeStatistical(startTime, endTime, province, city, area, OrderTypeEnum.HIGH_LAUNDRY.getCode());
    }

    /*会员消费统计_家具*/
    @RequestMapping("/furnitureconsumestatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object furnitureConsumeStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                              @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                              @RequestParam(name = "area", defaultValue = "") String area,
                                              @RequestParam(name = "province", defaultValue = "") String province,
                                              @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.furnitureConsumeStatistical(startTime, endTime, province, city, area);
    }

    /*会员消费统计_商城*/
    @RequestMapping("/mallconsumestatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object mallConsumeStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                         @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                         @RequestParam(name = "area", defaultValue = "") String area,
                                         @RequestParam(name = "province", defaultValue = "") String province,
                                         @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.mallConsumeStatistical(startTime, endTime, province, city, area);
    }

    /*商品消费统计_洗衣*/
    @RequestMapping("/laundryproductstatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object laundryProductStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                            @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                            @RequestParam(name = "area", defaultValue = "") String area,
                                            @RequestParam(name = "province", defaultValue = "") String province,
                                            @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.laundryProductStatistical(startTime, endTime, province, city, area, OrderTypeEnum.LAUNDRY.getCode());
    }


    /*商品消费统计_高端洗护*/
    @RequestMapping("/highlaundryproductstatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object highLaundryProductStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                                @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                                @RequestParam(name = "area", defaultValue = "") String area,
                                                @RequestParam(name = "province", defaultValue = "") String province,
                                                @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.laundryProductStatistical(startTime, endTime, province, city, area, OrderTypeEnum.HIGH_LAUNDRY.getCode());
    }

    /*商品消费统计_小让商城*/
    @RequestMapping("/mallproductstatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object mallProductStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                         @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                         @RequestParam(name = "area", defaultValue = "") String area,
                                         @RequestParam(name = "province", defaultValue = "") String province,
                                         @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.mallProductStatistical(startTime, endTime, province, city, area);
    }

    /*商品消费统计_家具*/
    @RequestMapping("/furnitureproductstatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.CONSUME_STATISTICAL)
    public Object furnitureProductStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                              @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                              @RequestParam(name = "area", defaultValue = "") String area,
                                              @RequestParam(name = "province", defaultValue = "") String province,
                                              @RequestParam(name = "city", defaultValue = "") String city, Principal principal) {
        return statisticalService.furnitureProductStatistical(startTime, endTime, province, city, area);
    }

    /*收支明细统计*/
    @RequestMapping("/financestatistical")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_STATISTICAL)
    public Object financeStatistical(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                     @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,Principal principal) {

        return statisticalService.financeStatistical(startTime, endTime);
    }
}
