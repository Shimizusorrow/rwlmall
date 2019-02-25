package com.qunchuang.rwlmall.utils;

import com.qunchuang.rwlmall.bean.RoleVO;
import com.qunchuang.rwlmall.domain.Role;

/**
 * @author Curtain
 * @date 2018/5/18 9:55
 */
public class ConvertUtil {

    public static RoleVO convert(Role role){
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setAccountId(role.getAccountId());
        vo.setDepartment(role.getDepartment());
        vo.setFunctionAuthority(role.getFunctionAuthority());
        vo.setPhone(role.getPhone());
        vo.setRoleNumber(role.getRoleNumber());
        vo.setUsername(role.getUsername());
        return vo;
    }
}
