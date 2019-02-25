package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.Chargeback;
import com.qunchuang.rwlmall.domain.Refund;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.ChargebackService;
import com.qunchuang.rwlmall.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/11/14 8:46
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/chargeback")
public class ChargebackController {

    @Autowired
    private ChargebackService chargebackService;

    @PostMapping("/save")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_CHARGE_BACK)
    public Object save(@RequestBody Chargeback chargeback, Principal principal) {
        return chargebackService.save(chargeback, principal);
    }

    @RequestMapping("/findall")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_CHARGE_BACK)
    public Object findAll(@RequestParam(name = "size", defaultValue = "8") Integer size,
                          @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return chargebackService.findAll(pageable);
    }

    @RequestMapping("/findbyphone")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_CHARGE_BACK)
    public Object findByPhone(@RequestParam("phone") String phone,
                              @RequestParam(name = "size", defaultValue = "8") Integer size,
                              @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return chargebackService.findByUserPhone(phone, pageable);
    }

    @RequestMapping("/findone")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_CHARGE_BACK)
    public Object findOne(@RequestParam("chargebackId") String chargebackId, Principal principal) {
        return chargebackService.findOne(chargebackId);
    }
}
