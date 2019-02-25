package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.PlatformText;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.PlatformTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/28 14:54
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/platformtext")
public class PlatformTextController {

    @Autowired
    private PlatformTextService platformTextService;


    /*保存*/
    @PostMapping("/save")
    @RoleAuthority(RoleAuthorityFunctionEnum.PLATFORM_TEXT)
    public Object save(@RequestBody PlatformText platformText,Principal principal){
        return platformTextService.save(platformText);
    }

    /*修改信息*/
    @PostMapping("/update")
    @RoleAuthority(RoleAuthorityFunctionEnum.PLATFORM_TEXT)
    public Object update(@RequestParam("platformtextid") String platformTextId,@RequestBody PlatformText platformText,Principal principal){
        return platformTextService.update(platformText, platformTextId);
    }

    /*按类型查找*/
    @RequestMapping("/findbytype")
    public Object findByType(@RequestParam("type") Integer type){
        return platformTextService.findByType(type);
    }
}
