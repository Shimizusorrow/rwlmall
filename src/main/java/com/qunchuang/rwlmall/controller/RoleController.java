package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.bean.RoleVO;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.service.RoleService;
import com.qunchuang.rwlmall.utils.ListToPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/19 10:15
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /*添加一个新代理商角色*/
    @PostMapping("/saveagent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object saveAgent(@RequestBody Role role) {
        roleService.saveAgent(role);
        return null;
    }

    /*添加一个总部管理员*/
    @PostMapping("/savegeneralhq")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object saveGeneralHQ(@RequestBody Role role) {
        roleService.saveGeneralHQ(role);
        return null;
    }

    /*添加代理商管理员*/
    @PostMapping("/savegeneralagent")
    @PreAuthorize("hasRole('ROLE_AGENT')")
    public Object saveGeneralAgent(@RequestBody Role role) {
        roleService.saveGeneralAgent(role);
        return null;
    }

    /*添加门店*/
    @PostMapping("/savestore")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object saveStore(@RequestBody Role role) {
        roleService.saveStore(role);
        return null;
    }


    /*修改密码*/
    @RequestMapping("/modifypassword")
    public Object modifyPassword(@RequestParam String username, @RequestParam String password, Principal principal) {
        return roleService.modifyPassword(username, password, principal);
    }

    /*修改权限*/
    @RequestMapping("/modifypermission")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object modifyPermission(String username, String permission, Principal principal) {
        roleService.modifyPermission(username, permission, principal);
        return null;
    }

    /*按权限类型查找*/
    @RequestMapping("/findbyrolenumber")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object findByRoleNumber(@RequestParam Integer number,
                                   @RequestParam(name = "size", defaultValue = "5") Integer size,
                                   @RequestParam(name = "page", defaultValue = "1") Integer page) {

        return ListToPageUtil.convert(roleService.findByRoleNumber(number),page,size);
    }

    /*按权限类型和代理商查找*/
    @RequestMapping("/findbyrolenumberandaccountid")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object findByRoleNumberAndAccountId(@RequestParam Integer number, @RequestParam("accountid") String accountId,
                                               @RequestParam(name = "size", defaultValue = "5") Integer size,
                                               @RequestParam(name = "page", defaultValue = "1") Integer page) {
        List<RoleVO> list = roleService.findByRoleNumberAndAccountId(number, accountId);

        return ListToPageUtil.convert(list,page,size);
    }

    /*查看自身信息*/
    @RequestMapping("/getselfinfo")
    public Object getSelfInfo(Principal principal) {
        return roleService.getSelfInfo(principal);
    }

    /*删除*/
    @RequestMapping("/deleterole")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    public Object deleteRole(@RequestParam String username, Principal principal) {
        roleService.delete(username, principal);
        return null;
    }

    /*通过id查询*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("roleid") String roleId,Principal principal) {
        return roleService.findOne(roleId);
    }

//    @RequestMapping("/initadmin")
//    public Object initAdmin(){
//        roleService.initAdmin();
//        return null;
//    }

}
