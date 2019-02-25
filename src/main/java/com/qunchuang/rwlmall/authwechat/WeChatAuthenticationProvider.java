package com.qunchuang.rwlmall.authwechat;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class WeChatAuthenticationProvider implements AuthenticationProvider {

    public WeChatAuthenticationProvider() {

    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        WeChatAuthenticationToken authenticationToken = (WeChatAuthenticationToken) authentication;
        String userId = (String) authenticationToken.getPrincipal();
        if ("".equals(userId)) {
            throw new BadCredentialsException("Unable to obtain open information");
        }
        WeChatAuthenticationToken authenticationResult = new WeChatAuthenticationToken(userId,true);
        return authenticationResult;
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return WeChatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
