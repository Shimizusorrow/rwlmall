package com.qunchuang.rwlmall.anntations;

import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleAuthority {
    RoleAuthorityFunctionEnum value();
}
