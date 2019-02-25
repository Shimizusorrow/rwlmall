package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.GuideCard;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.GuideCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/24 14:55
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/guidecard")
public class GuideCardController {

    //导卡模块 取消

//    @Autowired
//    private GuideCardService guideCardService;
//
//    @PostMapping("/save")
//    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
//    public Object save(@RequestBody GuideCard guideCard, Principal principal) {
//        return guideCardService.save(guideCard, principal);
//    }
//
//    @RequestMapping("/findall")
//    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
//    public Object findAll(@RequestParam(name = "size", defaultValue = "8") Integer size,
//                          @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createtime"));
//        return guideCardService.findAll(pageable);
//    }
//
//    @RequestMapping("/findbyphone")
//    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
//    public Object findByPhone(@RequestParam("phone") String phone,
//                              @RequestParam(name = "size", defaultValue = "8") Integer size,
//                              @RequestParam(name = "page", defaultValue = "1") Integer page, Principal principal) {
//        Pageable pageable = PageRequest.of(page - 1, size,Sort.by(Sort.Direction.DESC, "createtime"));
//        return guideCardService.findByUserPhone(phone, pageable);
//    }
//
//    @RequestMapping("/findone")
//    @RoleAuthority(RoleAuthorityFunctionEnum.FINANCIAL_MANAGE)
//    public Object findOne(@RequestParam("guidecardid") String guideCardId, Principal principal) {
//        return guideCardService.findOne(guideCardId);
//    }
}
