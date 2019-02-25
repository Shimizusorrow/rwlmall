package com.qunchuang.rwlmall;

import com.qunchuang.rwlmall.domain.Role;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public static String currentAuditor = "admin";
    private String operator;


    @Override
    public Optional<String> getCurrentAuditor() {
        try{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            /*一种是管理员*/
            if (principal instanceof Role){
                Role role = (Role)principal;
                operator = role.getUsername();
            }
            /*一种是客户端用户*/
            else {
                operator  = principal.toString();
            }

        }catch (NullPointerException e){
            return Optional.of(currentAuditor);
        }
            return Optional.of(operator);

           

    }

}