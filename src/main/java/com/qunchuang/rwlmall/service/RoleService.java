package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.RoleVO;
import com.qunchuang.rwlmall.domain.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/19 9:11
 */
public interface  RoleService extends UserDetailsService {

    /*保存代理商角色*/
    void saveAgent(Role role);

    /*保存门店角色*/
    void saveStore(Role role);

    /*保存总部普通管理角色*/
    void saveGeneralHQ(Role role);

    /*保存代理商普通管理角色*/
    void saveGeneralAgent(Role role);

    /*修改密码*/
    Boolean modifyPassword(String username, String password,Principal principal);

    /*修改权限*/
    void modifyPermission(String username, String permission, Principal principal);

    /*按权限类型查找*/
    List<RoleVO> findByRoleNumber(Integer number);

    /*按权限类型和代理商查找*/
    List<RoleVO> findByRoleNumberAndAccountId(Integer number, String accountId);

    /*通过id查询*/
    RoleVO findOne(String roleId);

    /*删除*/
    void delete(String username,Principal principal);

    RoleVO getSelfInfo(Principal principal);

    void initAdmin();
}
