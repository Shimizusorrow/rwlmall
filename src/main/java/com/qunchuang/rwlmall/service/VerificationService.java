package com.qunchuang.rwlmall.service;

import com.aliyuncs.exceptions.ClientException;

public interface VerificationService {

    /*获取并向用户发送验证码*/
    String getCode(String phoneNumber, String userId) throws ClientException;

    /*验证用户输入的验证码*/
    void verifyCode(String code, String phoneNumber, String userId);
}
