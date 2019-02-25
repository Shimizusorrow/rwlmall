package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.service.RechargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/16 13:57
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/rechargeorder")
public class RechargeOrderController {

    @Autowired
    private RechargeOrderService rechargeOrderService;

    @RequestMapping("/findone")
    public Object findOne(@RequestParam("orderid") String orderId){
        return rechargeOrderService.findOne(orderId);
    }

    @RequestMapping("/create")
    public Object create(@RequestParam("rechargekey") String rechargeKey, Principal principal){
        return rechargeOrderService.create(rechargeKey,principal.getName());
    }

}
