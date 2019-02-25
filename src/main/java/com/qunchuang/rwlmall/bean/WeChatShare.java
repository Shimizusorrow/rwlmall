package com.qunchuang.rwlmall.bean;

import lombok.Data;

/**
 * @author Curtain
 * @date 2018/8/31 9:21
 */
@Data
public class WeChatShare {

    /**
     * appid
     */
    private String appid;

    /**
     * 签名时间戳
     */
    private String timestamp;

    /**
     * 随机串
     */
    private String nonceStr;

    /**
     * sha1签名
     */
    private String signature;

}
