package com.qunchuang.rwlmall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Json;
import com.qunchuang.rwlmall.config.WeChatAccountConfig;
import com.qunchuang.rwlmall.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Curtain
 * @date 2018/8/22 8:51
 */
@Slf4j
public class WeChatUtil {

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public static String getToken(String appId, String secret) {
        String url = ACCESS_TOKEN_URL + "&appid=" + appId + "&secret=" + secret;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        JSONObject parse = (JSONObject) JSONObject.parse(result);
        String access_token = (String) parse.get("access_token");

        return access_token;


    }


    public static void sendMessage(String token, String openid, String content, String number, Long amount, String status, Long createTime) {

        String url = SEND_MESSAGE + token;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String time = sdf.format(new Date(createTime));

        Map map = new HashMap<>();

        map.put("openid", openid);
        map.put("title", "您的订单状态已更新！");
        map.put("content", content);
        map.put("ordernumber", number);
        map.put("amout", "¥" + amount / 100.0 + "元");
        map.put("status", status);
        map.put("createtime", time);
        map.put("remark", "如有疑问，请咨询400-0878-315");

        String json = "{\"touser\":\"" + map.get("openid") + "\",\"template_id\":\"4vBxtiSiXJjAVyRBk5HxF0865YYMW1s_rw0Ql0JmRjc\",\"data\":{\"first\":{\"value\":\"" + map.get("title") + "\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"" + map.get("ordernumber") + "\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"" + map.get("content") + "\",\"color\":\"#173177\"},\"keyword3\":{\"value\":\"" + map.get("amout") + "\",\"color\":\"#173177\"},\"keyword4\":{\"value\":\"" + map.get("status") + "\",\"color\":\"#173177\"},\"keyword5\":{\"value\":\"" + map.get("createtime") + "\",\"color\":\"#173177\"},\"remark\":{\"value\":\"" + map.get("remark") + "\",\"color\":\"#173177\"}}}";

        Object o = JSONObject.parse(json);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, o, String.class);
        if (result.contains("40001")) {
            //重写获取token  并放入 redis
            RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");
            WeChatAccountConfig weChatAccountConfig = (WeChatAccountConfig) SpringUtil.getBean("weChatAccountConfig");

            String newToken = WeChatUtil.getToken(weChatAccountConfig.getAppId(), weChatAccountConfig.getAppSecret());

            redisTemplate.opsForValue().set("weChatToken", newToken);

            //重新推送
            sendMessage(newToken, openid, content, number, amount, status, createTime);
        }
        log.info(result);

    }

    public static void main(String[] args) {
//        String wx3c6699da59f7cb25 = getToken("wx3c6699da59f7cb25", "283696ca1e6fc9ef739b1b74cea6b3ce");
//        System.out.println(wx3c6699da59f7cb25);
//        sendMessage("1","1","1","1",1L,"1",1L);
    }
}
