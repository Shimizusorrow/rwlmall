package com.qunchuang.rwlmall.controller;


import com.aliyuncs.exceptions.ClientException;
import com.qunchuang.rwlmall.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/verify")
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/getcode")
    public Object getCode(@RequestParam("phonenumber") String phoneNumber, Principal principal) {
        try {
            verificationService.getCode(phoneNumber, principal.getName());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/verifycode")
    public Object verifyCode(String code, @RequestParam("phonenumber") String phoneNumber, Principal principal) {
        verificationService.verifyCode(code, phoneNumber, principal.getName());
        return "认证成功";
    }



}
