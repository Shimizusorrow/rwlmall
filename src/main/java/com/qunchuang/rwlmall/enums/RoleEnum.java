package com.qunchuang.rwlmall.enums;

import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Curtain
 * @date 2018/3/19 9:42
 */

@Getter
public enum RoleEnum {

    /*系统总部管理员*/
    ADMIN_ROLE(1, "ROLE_ADMIN"),

    /*代理商*/
    AGENT_ROLE(2, "ROLE_AGENT"),

    /*门店管理员*/
    STORE_ROLE(3, "ROLE_STORE"),

    /*普通总部管理员*/
    GENERAL_HQ_ROLE(4, "ROLE_GENERAL_HQ"),

    /*普通代理商管理员*/
    GENERAL_AGENT_ROLE(5, "ROLE_GENERAL_AGENT");


    private String message;
    private Integer code;

    RoleEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
