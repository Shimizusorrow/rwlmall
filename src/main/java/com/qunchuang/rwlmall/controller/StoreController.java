package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.domain.Store;
import com.qunchuang.rwlmall.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/4/17 10:53
 */


@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    /*通过门店id查询*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("storeid") String storeId) {
        return storeService.findOne(storeId);
    }

    /*保存*/
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object save(@RequestBody Store store) {
        return storeService.save(store);
    }

    /*修改门店信息*/
    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object update(@RequestParam("storeid") String storeId, @RequestBody Store store) {
        return storeService.update(storeId, store);
    }

    /*删除门店*/
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object delete(@RequestParam("storeid") String storeId) {
        //如果账号是代理商 那么只能修改代理商旗下的门店  其他的门店不允许
        storeService.delete(storeId);
        return null;
    }

    /*查找代理商所属门店*/
    @RequestMapping("/findbyagent")
    public Object findByAgentId(@RequestParam("agentid") String agentId,
                                @RequestParam(name = "size", defaultValue = "100") Integer size,
                                @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storeService.findByAgentId(agentId, pageable);
    }

    /*查找所有*/
    @RequestMapping("/findall")
    public Object findAll() {
        return storeService.findAll();
    }

    /*修改门店营业状态*/
    @RequestMapping("/modifystatus")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object modifyStatus(@RequestParam("storeid") String storeId) {
        storeService.modifyStatus(storeId);
        return null;
    }

    /*通过区域查找门店*/
    @RequestMapping("finddistributestore")
    public Object findDistributeStore(Principal principal) {
        return storeService.findDistributeStore(principal);
    }

}
