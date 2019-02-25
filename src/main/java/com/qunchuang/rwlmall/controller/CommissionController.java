package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.bean.Commission;
import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.CommissionService;
import com.qunchuang.rwlmall.service.RechargeService;
import com.qunchuang.rwlmall.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/9 15:20
 */


@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/commission")
public class CommissionController {

    @Autowired
    private CommissionService commissionService;

    /*设置提成*/
    @PostMapping("/setcommission")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GENERAL_HQ')")
    @RoleAuthority(RoleAuthorityFunctionEnum.COMMISSION)
    public Object setCommission(@RequestParam String key, @RequestBody Commission commission, Principal principal){
        commissionService.setCommission(key,commission);
        return null;
    }

    /*获取提成列表*/
    @RequestMapping("/getall")
    public Object getAll(){
        return commissionService.getAll();
    }

    /*获取指定指定提成*/
    @RequestMapping("/getcommission")
    public Object getCommission(@RequestParam String key){
        return commissionService.getCommission(key);
    }

    /*初始化redis数据*/
    @RequestMapping("/initreidsdata")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RoleAuthority(RoleAuthorityFunctionEnum.ACCOUNT_MANGER)
    public Object initRedisData(Principal principal){
        commissionService.initRedisData();
        return null;
    }
}
