package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.bean.FreightSet;
import com.qunchuang.rwlmall.enums.FreightCategoryEnum;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.FreightSetService;
import com.qunchuang.rwlmall.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/5/15 11:00
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/freightset")
public class FreightSetController {

    @Autowired
    private FreightSetService freightSetService;

    @PostMapping("/set")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GENERAL_HQ')")
    @RoleAuthority(RoleAuthorityFunctionEnum.FREIGHT_SET)
    public Object set(String key, @RequestBody FreightSet freightSet, Principal principal) {
        freightSetService.setFreightSet(key, freightSet);
        return null;
    }

    @RequestMapping("/get")
    public Object get() {
        return freightSetService.getFreightSet();
    }

    @RequestMapping("/findbykey")
    public Object get(String key) {
        return freightSetService.getFreightSet(key);
    }

    @RequestMapping("/getall")
    public Object getAll() {
        return freightSetService.getAll();
    }

    @RequestMapping("/init")
    public void init() {
        FreightSet freightSet = new FreightSet();
        freightSet.setFreight(2000L);
        freightSet.setThreshold(5000L);
        freightSet.setKey(FreightCategoryEnum.LAUNDRY.getKey());
        freightSetService.setFreightSet(FreightCategoryEnum.LAUNDRY.getKey(), freightSet);

        freightSet = new FreightSet();
        freightSet.setFreight(2000L);
        freightSet.setThreshold(5000L);
        freightSet.setKey(FreightCategoryEnum.MALL.getKey());
        freightSetService.setFreightSet(FreightCategoryEnum.LAUNDRY.getKey(), freightSet);
    }
}
