package com.qunchuang.rwlmall.aspect;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.utils.RoleUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@RestController
public class RoleAuthorityCheckAspect {

    @Pointcut("execution(public * com.qunchuang.rwlmall.controller.*.*(..)) " +
            //@annotation 表示对另外一个条件进行AOP处理
            " && @annotation(roleAuthority)")

    public void init(RoleAuthority roleAuthority) {
    }

    @Before("init(roleAuthority)")
    public void doBefore(JoinPoint joinPoint, RoleAuthority roleAuthority) {
        Optional<Object> priOpt = Arrays.stream(joinPoint.getArgs()).filter(arg -> arg.getClass().equals(UsernamePasswordAuthenticationToken.class)).findAny();
        if (priOpt.isPresent()) {
            Principal principal = (Principal) priOpt.get();
            RoleUtil.checkRoleFunction(principal, roleAuthority.value().getCode());
        }

    }
}
