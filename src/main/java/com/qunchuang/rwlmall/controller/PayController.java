package com.qunchuang.rwlmall.controller;


import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.WxPayServiceImpl;
import com.qunchuang.rwlmall.service.PayService;
import com.qunchuang.rwlmall.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;


@RequestMapping("/pay")
@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@Slf4j
public class PayController {

    @Autowired
    public PayService payService;

//    @Autowired
//    public WxPayH5Config wxPayH5Config;


//    @RequestMapping("/query")
//    public Object query(@RequestParam("id") String id){
//        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
//        wxPayService.setWxPayH5Config(wxPayH5Config);
//        wxPayService.query(id);
//        return null;
//    }

    @RequestMapping("/wechatpay")
    public Object weChatPay(@RequestParam("orderid") String orderId) {

//        PayResponse payResponse= payService.weChatPay(orderId);
//
//        HashMap<String, PayResponse> map = new HashMap<>();
//        map.put("payResponse",payResponse);
//        return new ModelAndView("pay/create",map);

        return payService.weChatPay(orderId);
    }

    @RequestMapping("/balancepay")
    public Object balancePay(@RequestParam("orderid") String orderId) {
        payService.balancePay(orderId);
        return null;
    }

    @RequestMapping("/cardpay")
    public Object cardPay(Principal principal,@RequestParam("orderid") String orderId){
        payService.cardPay(orderId,principal.getName());
        return null;
    }

    @PostMapping("/notify")
    private ModelAndView notify(@RequestBody String notifyData) {
        PayService  payService = (PayService) SpringUtil.getBean("payServiceImpl");
        payService.notify(notifyData);
        //返回微信处理结果
        return new ModelAndView("pay/success");

    }

    @RequestMapping("/wechatshare")
    public Object weChatShare(){
        return payService.generate();
    }


}
