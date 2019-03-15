package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/9 15:13
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    /*设置充值奖励*/
    @PostMapping("/setrecharge")
    @RoleAuthority(RoleAuthorityFunctionEnum.RECHARGE)
    public Object setRecharge(@RequestParam String key, @RequestBody Recharge recharge, Principal principal) {
        rechargeService.setRecharge(key, recharge);
        return null;
    }

    /*获取充值奖励列表*/
    @RequestMapping("/getall")
    public Object getAll() {
        return rechargeService.getAll();
    }

    /*获取指定充值奖励*/
    @RequestMapping("/getrecharge")
    public Recharge getRecharge(@RequestParam String key) {
        return rechargeService.getRecharge(key);
    }

    @RequestMapping("/init")
    public void init() {
        Recharge recharge = new Recharge();
        for (int i = 1; i < 5; i++) {
            recharge.setKey("rwlRecharge" + i);
            recharge.setPayMoney(2000L * i);
            recharge.setRewardMoney(500L * i);
            rechargeService.setRecharge("rwlRecharge" + i, recharge);
        }
    }
}
