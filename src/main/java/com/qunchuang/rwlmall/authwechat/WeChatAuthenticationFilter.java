package com.qunchuang.rwlmall.authwechat;


import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WeChatAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public static final String SPRING_SECURITY_FORM_USER_ID_KEY = "userId";

    private String userId = SPRING_SECURITY_FORM_USER_ID_KEY;


    private boolean postOnly = true;

    public WeChatAuthenticationFilter() {
        super(new AntPathRequestMatcher("/wechat/login", "GET"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !"GET".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String id = obtainCode(request);

        if (id == null) {
            id = "";
        }

        id = id.trim();

        WeChatAuthenticationToken authRequest = new WeChatAuthenticationToken(id, false);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter("id");
    }


    protected void setDetails(HttpServletRequest request,
                              WeChatAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public final String getUserId() {
        return userId;
    }

}
