package com.qunchuang.rwlmall.controller;


import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


/**
 * 原来使用微信的一套
 */

@Controller
@RequestMapping("/wechat")
@Slf4j
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class WeChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${redirecturl}")
    private String url;

    @Value("${authurl}")
    private String authUrl;

    @RequestMapping(value = "/authorize")
    private String authorize(String returnUrl) {
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, returnUrl);

        return "redirect:" + redirectUrl;
    }


    @GetMapping(value = "/userinfo")
    private Object userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl) {

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;

        String userId = "";

        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            User user;

            //todo 加锁 避免用户多次请求导致 保存两次。     用户量大之后 可以考虑取消synchronized redis锁
            synchronized (Object.class) {
            /*只有当用户不存在与数据库中   才需要保存*/
                if (!userService.existsByOpenid(wxMpOAuth2AccessToken.getOpenId())) {
                    User result = new User();
                    WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
                    BeanUtils.copyProperties(wxMpUser, result);
                    result.setName(wxMpUser.getNickname());
                    result.setOpenid(wxMpOAuth2AccessToken.getOpenId());
                    result.setGender(wxMpUser.getSex());
                    result.setBalance(0L);
                    user = userService.save(result);
                } else {
                    user = userService.findByOpenid(wxMpOAuth2AccessToken.getOpenId());
                }

            }
            /*表示微信已经认证成功  下面获取token*/
            userId = user.getId();
            String token = auth(userId);

            returnUrl += (returnUrl.contains("?") ? "&" : "?") + "x-auth-token=" + token;


        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
//            auth(userId);
            throw new RwlMallException(ResultExceptionEnum.WX_MP_ERROR, e.getError().getErrorMsg());
        }

        return "redirect:" + returnUrl;

    }

    private String auth(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(authUrl + "?id=" + userId, String.class);
    }

    @GetMapping(value = "/jsapisignature")
    @ResponseBody
    private Object createJsapiSignature(@RequestParam("url") String url) throws WxErrorException {
        return this.wxMpService.createJsapiSignature(url);
    }

}
