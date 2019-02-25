package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Curtain
 * @date 2018/5/18 9:51
 * 角色结果返回
 */

@Getter
@Setter
public class RoleVO {

    /*id*/
    private String id;

    /*用户名*/
    private String username;

    /*部门*/
    private String department;

    /*联系电话*/
    private String phone;

    /*角色权限对应的数字*/
    private Integer roleNumber;

    /*功能权限*/
    private String functionAuthority;

    /*角色id  代理商  或者 门店  */
    private String accountId;


}
