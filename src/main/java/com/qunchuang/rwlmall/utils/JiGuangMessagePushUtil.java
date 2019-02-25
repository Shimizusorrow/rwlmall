package com.qunchuang.rwlmall.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagAliasResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * 推送模块删除 不支持  2019-2-25
 * 极光消息推送
 * @author Curtain
 * @date 2018/8/17 8:56
 */
@Slf4j
public class JiGuangMessagePushUtil {
    public static void sendMessage(String tag,String content){

       return;
    }
}
