package com.qunchuang.rwlmall.utils;

import com.qunchuang.rwlmall.bean.CardInfo;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员卡模块 删除 不支持 2019-02-25
 * @author Curtain
 * @date 2018/6/14 14:24
 */
@Slf4j
public class CardPayUtil {
    /**
     * 卡查询
     *
     * @param cno 会员卡号
     * @return
     */
    public static Long getBalance(String cno) {

        return null;
    }

    /**
     * 卡查询
     *
     * @param phone 手机号
     * @return
     */
    public static List<CardInfo> query(String phone) {
        return null;
    }

    /**
     * 卡支付
     *
     * @param cid    id
     * @param cno    会员卡号
     * @param amount 金额
     * @return
     */
    public static Boolean pay(String cid, String cno, String amount) {

        return null;
    }


    /**
     * 发送请求 执行卡相关操作
     *
     * @param url api地址
     * @param map 参数
     * @return
     */
    private static String SendRequest(String url, Map<String, String> map) {

        return null;
    }

    /**
     * 加密
     *
     * @param map
     */
    private static void ToMD5Sign(Map<String, String> map) {

    }

    /**
     * 解析卡查询结果xml  list
     *
     * @param xml
     * @return
     */
    public static List<CardInfo> parseCardQueryXML(String xml) {

        return null;
    }

    /**
     * 解析卡支付结果xml
     *
     * @param xml
     * @return
     */
    public static Boolean parseCardPayXML(String xml) {
        return true;
    }

    /**
     * 解析卡查询结果xml
     *
     * @param xml
     * @return
     */
    public static Long parseXML(String xml) {

        return null;

    }

}
