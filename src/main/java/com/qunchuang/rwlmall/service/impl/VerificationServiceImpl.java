package com.qunchuang.rwlmall.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.bcloud.msg.http.HttpSender;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.service.UserService;
import com.qunchuang.rwlmall.service.VerificationService;
import com.qunchuang.rwlmall.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    @Override
    public String getCode(String phoneNumber, String userId) throws ClientException {
        String code = NumberUtil.createRandomNum(6);
        String content = "【让我来】您的验证码是" + code;

        //发送成功保存code
        redisTemplate.opsForValue().set(userId + phoneNumber, code);

        return code;
    }

    @Override
    public void verifyCode(String code, String phoneNumber, String userId) {
        String key = userId + phoneNumber;
        String rs = (String) redisTemplate.opsForValue().get(key);

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


    }


}

