package com.qunchuang.rwlmall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "wechat")
public class WeChatAccountConfig {

    private String appId;

    private String appSecret;

    private String mchId;

    private String mchKey;

    private String keyPath;

    private String notifyUrl;
}
