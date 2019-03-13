package com.qunchuang.rwlmall.service.impl;

import com.alibaba.fastjson.JSONException;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.bcloud.msg.http.HttpSender;
import com.qunchuang.rwlmall.RwlmallApplication;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.service.UserService;
import com.qunchuang.rwlmall.service.VerificationService;
import com.qunchuang.rwlmall.utils.ALiYunMessageUtil;
import com.qunchuang.rwlmall.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    @Value("${account}")
    String account;

    @Value("${password}")
    String password;

    @Value("${sendurl}")
    String sendurl;

    @Override
    public void getCode(String phoneNumber, String userId) throws ClientException {
        //采用云测TestIn平台来发送验证码
        String code = NumberUtil.createRandomNum(6);
        String content = "【让我来】您的验证码是" + code;

        try {
            HttpSender.send(sendurl, account, password, phoneNumber, content, false, "", "");
        } catch (Exception e) {
            throw new RwlMallException(ResultExceptionEnum.CODE_GAIN_FAILURE);
        }

        //发送成功保存code
        redisTemplate.opsForValue().set(userId + phoneNumber, code);
    }

    @Override
    public void verifyCode(String code, String phoneNumber, String userId) {
        String key = userId + phoneNumber;
        String rs = (String) redisTemplate.opsForValue().get(key);

        if (code.equals(rs)) {

            User user = userService.findOne(userId);


            //如果用户未绑定   那么第一次绑定手机号
            if (!user.getBinding()){

                User phone;
                try{
                    phone = userService.findByPhone(phoneNumber);
                }catch (RwlMallException e){
                    phone = null;
                }

                User registerPhone = userService.findByRegisterPhone(phoneNumber);
                if (phone!=null ||registerPhone!=null ){
                    throw new RwlMallException(ResultExceptionEnum.PHONE_IS_REGISTER);
                }
                user.setBinding(true);
                user.setPhone(phoneNumber);
                user.setRegisterPhone(phoneNumber);
                userService.save(user);
            }

            //修改新手机号的条件  1.用户已经绑定手机号  2.当前手机号等于用户绑定的手机号
            if (user.getBinding() && phoneNumber.equals(user.getPhone())) {
                //半小时内用户拥有修改手机号权限
                redisTemplate.opsForValue().set(userId + "phone", String.valueOf(System.currentTimeMillis()));
            }

            //其他情况  直接验证通过  删除验证码即可
            redisTemplate.delete(key);
        } else {
            throw new RwlMallException(ResultExceptionEnum.DO_NOT_PASS);
        }

    }


}

