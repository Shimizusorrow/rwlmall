package com.qunchuang.rwlmall.config;

import com.qunchuang.rwlmall.utils.MD5Util;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Curtain
 * @date 2018/3/19 14:08
 */


@Component("passwordEncoder")
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence arg0) {
        return arg0.toString();
    }

    @Override
    public boolean matches(CharSequence arg0, String arg1) {
        try{
            return MD5Util.verify(arg0.toString(),arg1);
        }catch (Exception e){
            throw new BadCredentialsException("认证失败");
        }

    }
}
