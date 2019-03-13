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
 * @author Curtain
 * @date 2018/6/14 14:24
 */
@Slf4j
public class CardPayUtil {

    /**
     * api地址
     */
    private final static String API_URL = "http://120.26.238.89:21039";

    /**
     * 秘钥
     */
    private final static String SECRET_KEY = "mhkabcdefg0123456";

//    /**
//     * 卡查询
//     *
//     * @param cid  id
//     * @param cno  会员卡号
//     * @return
//     */
//    public static CardInfo query(String cid, String cno) {
//        //请求接口url
//        String url = API_URL + "/partner/Get";
//
//        //构造请求参数
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("v", "1");
//        map.put("sid", "101");
//        map.put("d", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        map.put("term", "158");
//        map.put("oper", "admin");
//        map.put("shop", "170");
//        map.put("format", "xml");
//        map.put("sn", "2018062500");
//        map.put("cid", cid);
//        map.put("cno", cno);
//
//        //加密
//        ToMD5Sign(map);
//
//        //发送请求
//        String result = SendRequest(url, map);
//
//        //解析并返回结果
//        return parseCardQueryXML(result);
//    }

    /**
     * 卡查询
     *
     * @param cno 会员卡号
     * @return
     */
    public static Long getBalance(String cno) {
        //请求接口url
        String url = API_URL + "partner/GetProfile";

        //构造请求参数
        Map<String, String> map = new LinkedHashMap<>();
        map.put("v", "1");
        map.put("sid", "101");
        map.put("d", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("term", "158");
        map.put("oper", "admin");
        map.put("shop", "170");
        map.put("format", "xml");
        map.put("cno", cno);

        //加密
        ToMD5Sign(map);

        //发送请求
        String result = SendRequest(url, map);

        //解析并返回结果
        return parseXML(result);
    }

    /**
     * 卡查询
     *
     * @param phone 手机号
     * @return
     */
    public static List<CardInfo> query(String phone) {
        //请求接口url
        String url = API_URL + "partner/GetCardsByMob";

        //构造请求参数
        Map<String, String> map = new LinkedHashMap<>();
        map.put("v", "1");
        map.put("sid", "101");
        map.put("d", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("term", "158");
        map.put("oper", "admin");
        map.put("shop", "170");
        map.put("sn", "2018081000");
        map.put("format", "xml");
        map.put("mob", phone);

        //加密
        ToMD5Sign(map);

        //发送请求
        String result = SendRequest(url, map);

        //解析并返回结果
        return parseCardQueryXML(result);
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
        //请求接口url
        String url = API_URL + "/partner/Pay";

        //构造请求参数
        Map<String, String> map = new LinkedHashMap<>();
        map.put("v", "1");
        map.put("sid", "101");
        map.put("d", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("term", "158");
        map.put("oper", "admin");
        map.put("shop", "170");
        map.put("format", "xml");
        map.put("sn", "2018081000");
        map.put("cid", cid);
        map.put("cno", cno);
        map.put("amt", amount);

        //加密
        ToMD5Sign(map);

        //发送请求
        String result = SendRequest(url, map);

        //解析并返回结果
        return parseCardPayXML(result);
    }


    /**
     * 发送请求 执行卡相关操作
     *
     * @param url api地址
     * @param map 参数
     * @return
     */
    private static String SendRequest(String url, Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        //设置请求头  因为API似乎只支持 表单提交数据
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        for (String key : map.keySet()) {
            params.add(key, map.get(key));
        }
        //组拼好的请求数据   添加到HttpEntity中
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(params, headers);

        //用RestTemplate 发送HTTP请求
        RestTemplate client = new RestTemplate();
        ResponseEntity<String> response = client.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }

    /**
     * 加密
     *
     * @param map
     */
    private static void ToMD5Sign(Map<String, String> map) {
        //对请求参数 进行排序
        SortedMap<String, String> sortedMap = new TreeMap<>(map);

        //拼接请求数据
        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = map.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key).append("=").append(value);
            }
        }

        //最后添加秘钥
        toSign.append(SECRET_KEY);
        //MD5加密
        String str = MD5Util.getMD5Str(toSign.toString());
        //得到加密后数据   添加到请求参数中
        map.put("sig", str);
    }

    /**
     * 解析卡查询结果xml  list
     *
     * @param xml
     * @return
     */
    public static List<CardInfo> parseCardQueryXML(String xml) {
        SAXBuilder builder = new SAXBuilder();
        Document readDoc = null;
        Reader in = new StringReader(xml);  //要先转换成Reader才能装载的

        try {
            readDoc = builder.build(in);  //把xml字符串装载.
        } catch (JDOMException e) {
            log.error("雄伟卡接口 xml解析：" + e.getMessage());
        } catch (IOException e) {
            log.error("雄伟卡接口 xml解析：" + e.getMessage());
        }
        Element element = readDoc.getRootElement();
        Element code = element.getChild("code");
        if (!("1".equals(code.getValue()))) {
            throw new RwlMallException(ResultExceptionEnum.XW_CARD_NOT_EXIST);
        }

        List<CardInfo> result = new ArrayList<>();
        List list = element.getChild("cards").getChildren("item");
        for (int i = 0; i < list.size(); i++) {
            CardInfo cardInfo = new CardInfo();

            Element item = (Element) list.get(i);
            Element type = item.getChild("type");
            Element cid = item.getChild("id");
            Element cno = item.getChild("no");
            Element balance = item.getChild("balance");
            Element status = item.getChild("state");
            Element locked = item.getChild("locked");
            Element discount = item.getChild("discount");

            Element profile = element.getChild("profile");
            Element mob = profile.getChild("mob");

            cardInfo.setAmount((long) (Double.valueOf(balance.getValue()) * 100));
            cardInfo.setType(type.getValue());
            //todo if mob = null   setmobile = phone
            cardInfo.setMobile(mob.getValue());
            cardInfo.setCid(cid.getValue());
            cardInfo.setCno(cno.getValue());
            cardInfo.setStatus(Integer.valueOf(status.getValue()));
            cardInfo.setLocked(Integer.valueOf(locked.getValue()));
            cardInfo.setDiscount(Integer.valueOf(discount.getValue()));

            result.add(cardInfo);

        }

//        Element item = element.getChild("cards").getChild("item");


        return result;
    }

    /**
     * 解析卡支付结果xml
     *
     * @param xml
     * @return
     */
    public static Boolean parseCardPayXML(String xml) {
        SAXBuilder builder = new SAXBuilder();
        Document readDoc = null;
        Reader in = new StringReader(xml);  //要先转换成Reader才能装载的

        try {
            readDoc = builder.build(in);  //把xml字符串装载
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element element = readDoc.getRootElement();
        Element code = element.getChild("code");
        if (!("1".equals(code.getValue()))) {
            throw new RwlMallException(ResultExceptionEnum.XW_CARD_PAY_FAIL);
        }

        return true;
    }

    /**
     * 解析卡查询结果xml
     *
     * @param xml
     * @return
     */
    public static Long parseXML(String xml) {
        SAXBuilder builder = new SAXBuilder();
        Document readDoc = null;
        Reader in = new StringReader(xml);  //要先转换成Reader才能装载的

        try {
            readDoc = builder.build(in);  //把xml字符串装载.
        } catch (JDOMException e) {
            log.error("雄伟卡接口 xml解析：" + e.getMessage());
        } catch (IOException e) {
            log.error("雄伟卡接口 xml解析：" + e.getMessage());
        }
        Element element = readDoc.getRootElement();
        Element code = element.getChild("code");
        if (!("1".equals(code.getValue()))) {
            throw new RwlMallException(ResultExceptionEnum.XW_CARD_NOT_EXIST);
        }

        Element profile = element.getChild("profile");

        Element balance = profile.getChild("balance");

        return (long) (Double.valueOf(balance.getValue()) * 100);

    }

    public static void main(String[] args) {
        System.out.println(query("13868318186"));
//        System.out.print(getBalance("5770043694"));

    }
}
