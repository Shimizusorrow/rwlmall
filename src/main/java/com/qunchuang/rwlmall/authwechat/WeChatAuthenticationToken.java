package com.qunchuang.rwlmall.authwechat;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class WeChatAuthenticationToken extends AbstractAuthenticationToken {


    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final Object principal;


    public WeChatAuthenticationToken(Object principal, boolean b) {
        super(null);
        this.principal = principal;
        setAuthenticated(b);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
