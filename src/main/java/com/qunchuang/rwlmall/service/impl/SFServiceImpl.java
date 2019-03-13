package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.ExpressInfo;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.Store;
import com.qunchuang.rwlmall.enums.SFStatusEnum;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.SFUtil;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/27 8:31
 */

@Service
public class SFServiceImpl implements SFService {

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Autowired StoreService storeService;


    @Override
    public String dispatchOrder(String orderId,String storeId, Integer status) {

        Store store;
        LaundryOrder laundryOrder = laundryOrderService.findOne(orderId);

        //1.拼接xml
        Element request = new Element("Request");
        Element head = new Element("Head");
        Element body = new Element("Body");
        Element order = new Element("Order");
//        Element cargo = new Element("Cargo");

        request.addContent(head);
        request.addContent(body);
        head.addContent("SLKJ2019");
        body.addContent(order);
//        order.addContent(cargo);

        request.setAttribute("service", "OrderService");
        request.setAttribute("lang", "zh-CN");

        /*订单号*/
        order.setAttribute("orderid", orderId);

        /*快件产品类型 1标准快递   2顺丰特惠*/
        order.setAttribute("express_type", "1");

        /*如果是收*/
        if (status.equals(SFStatusEnum.COLLECT.getCode())){
            store = storeService.findOne(storeId);
            /*寄件人信息*/
            order.setAttribute("j_province", laundryOrder.getProvince());
            order.setAttribute("j_city", laundryOrder.getCity());
            order.setAttribute("j_company", "个人");
            order.setAttribute("j_contact", laundryOrder.getName());
            order.setAttribute("j_tel", laundryOrder.getPhone());
            order.setAttribute("j_address", laundryOrder.getAddress());

            /*收件人信息*/
            order.setAttribute("d_province", store.getProvince());
            order.setAttribute("d_city", store.getCity());
            order.setAttribute("d_company", "温州莫好克洗护有限公司");
            order.setAttribute("d_contact", store.getPeople());
            order.setAttribute("d_tel", store.getPhone());
            order.setAttribute("d_address", store.getArea()+store.getAddress());

            /*上面取件时间  格式YYYY-MM-DD HH24:MM:SS*/
            order.setAttribute("sendstarttime", laundryOrder.getDeliveryDate()+":00");
        }

        /*寄*/
        if (status.equals(SFStatusEnum.SEND.getCode())){
            store = storeService.findOne(storeId);
             /*寄件人信息*/
            order.setAttribute("j_province", store.getProvince());
            order.setAttribute("j_city", store.getCity());
            order.setAttribute("j_company", "温州莫好克洗护有限公司");
            order.setAttribute("j_contact", store.getPeople());
            order.setAttribute("j_tel", store.getPhone());
            order.setAttribute("j_address", store.getArea()+store.getAddress());

            /*收件人信息*/
            order.setAttribute("d_province", laundryOrder.getProvince());
            order.setAttribute("d_city", laundryOrder.getCity());
            order.setAttribute("d_company", "个人");
            order.setAttribute("d_contact", laundryOrder.getName());
            order.setAttribute("d_tel", laundryOrder.getPhone());
            order.setAttribute("d_address", laundryOrder.getAddress());
        }


        /*包裹数*/
        order.setAttribute("parcel_quantity", "1");
        /*支付方式*/
        order.setAttribute("pay_method", "1");
        /*顺丰月结卡号*/
        order.setAttribute("custid", "5775742659");
        /*要求收派员手持终端收件*/
        order.setAttribute("is_docall", "1");

//        /*货物名称*/
//        cargo.setAttribute("name", "Dresses");
//        /*数量*/
//        cargo.setAttribute("count", "1");
//        /*单位*/
//        cargo.setAttribute("unit","piece");
//        /*重量*/
//        cargo.setAttribute("weight", "1.000");
//        /*单价*/
//        cargo.setAttribute("amount", "1");
//        /*币别*/
//        cargo.setAttribute("currency", "CNY");
//        /*原产国别*/
//        cargo.setAttribute("source_area","CN");

        String repsXML = SFUtil.postRequest(request);

        SFUtil.parseDispatchOrder(repsXML);

        return "下单成功";
    }


    @Override
    public List<ExpressInfo> routingQuery(String orderId) {

        Element request = new Element("Request");
        Element head = new Element("Head");
        Element body = new Element("Body");
        Element routeRequest = new Element("RouteRequest");

        request.addContent(head);
        request.addContent(body);
        head.addContent("SLKJ2019");
        body.addContent(routeRequest);

        request.setAttribute("service", "RouteService");
        request.setAttribute("lang", "zh-CN");

        routeRequest.setAttribute("tracking_type", "2");
        routeRequest.setAttribute("method_type", "1");
        routeRequest.setAttribute("tracking_number", orderId);

        String repsXML = SFUtil.postRequest(request);

        List<ExpressInfo> expressInfoList = SFUtil.parseRoutingQueryXML(repsXML);

        return expressInfoList;
    }
}
