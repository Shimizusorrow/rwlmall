package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.GuideCard;
import com.qunchuang.rwlmall.domain.Refund;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/25 14:37
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @PostMapping("/save")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
    public Object save(@RequestBody Refund refund, Principal principal) {
        return refundService.save(refund, principal);
    }

    @RequestMapping("/findall")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
    public Object findAll(@RequestParam(name = "size", defaultValue = "8") Integer size,
                          @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return refundService.findAll(pageable);
    }

    @RequestMapping("/findbyphone")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
    public Object findByPhone(@RequestParam("phone") String phone,
                              @RequestParam(name = "size", defaultValue = "8") Integer size,
                              @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
        return refundService.findByUserPhone(phone, pageable);
    }

    @RequestMapping("/findone")
    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
    public Object findOne(@RequestParam("refundid") String refundId, Principal principal) {
        return refundService.findOne(refundId);
    }
}
