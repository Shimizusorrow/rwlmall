package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.Advertisement;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.AdvertisementService;
import com.qunchuang.rwlmall.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/3/13 15:31
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/advertisement")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    /*查询所属类别的广告*/
    @RequestMapping("/findbytype")
    public Object findByType(@RequestParam("type") Integer type, Principal principal) {
        return advertisementService.findByType(type);
    }

    /*保存一条广告*/
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GENERAL_HQ')")
    @RoleAuthority(RoleAuthorityFunctionEnum.ADVERTISEMENT)
    public Object save(@RequestBody Advertisement advertisement, Principal principal) {
        return advertisementService.save(advertisement);
    }

    /*修改一条广告*/
    @RequestMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GENERAL_HQ')")
    @RoleAuthority(RoleAuthorityFunctionEnum.ADVERTISEMENT)
    public Object update(@RequestParam("advertisementid") String advertisementId,
                         @RequestBody Advertisement advertisement, Principal principal) {
        return advertisementService.update(advertisementId, advertisement);
    }

    /*删除一条广告*/
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_GENERAL_HQ')")
    @RoleAuthority(RoleAuthorityFunctionEnum.ADVERTISEMENT)
    public Object delete(@RequestParam("advertisementid") String advertisementId, Principal principal) {
        advertisementService.delete(advertisementId);
        return null;
    }
}
