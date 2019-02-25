package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.LaundryOrderItem;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author Curtain
 * @date 2018/4/2 8:51
 */
public class LaundryLaundryOrderControllerTest {

    @Test
    public void create() throws Exception {
        String url = "http://localhost:8080/rwlmall/order/create";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Content-Type", "application/json;charset=utf-8");

        String jsonStr = "{\n" +
                "  \"name\":8,\n" +
                "  \"phone\":\"18156532485\",\n" +
                "  \"city\":\"温州\"\n" +
                "}";

        System.out.println(jsonStr);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }

    @Test
    public void createOrder(){
        LaundryOrder laundryOrder = new LaundryOrder();
        LaundryOrderItem laundryOrderItem = new LaundryOrderItem();
        LaundryProduct laundryProduct = new LaundryProduct();
        laundryOrderItem.setCount(10);
//        laundryOrder.set
    }

    @Test
    public void c() throws Exception {
        String url = "http://localhost:8080/rwlmall/laundryorder/singletoninbound";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Content-Type", "application/json;charset=utf-8");

        String jsonStr = "[{\n" +
                "\t\t\"id\": \"7docRWWSHCO1s99HVWm823A04\",\n" +
                "\t\t\"laundryProduct\": {\n" +
                "\t\t\t\"id\": \"u4UgXLb0HAipZPx5xzJt43A01\",\n" +
                "\t\t\t\"number\": \"524621021436\",\n" +
                "\t\t\t\"createactorid\": \"anonymousUser\",\n" +
                "\t\t\t\"updateactorid\": \"admin\",\n" +
                "\t\t\t\"updatetime\": 1527490464598,\n" +
                "\t\t\t\"createtime\": 1524621021435,\n" +
                "\t\t\t\"name\": \"十方俱灭\",\n" +
                "\t\t\t\"old_price\": 99900,\n" +
                "\t\t\t\"price\": 88800,\n" +
                "\t\t\t\"logo\": \"152462101280164439.jpg\",\n" +
                "\t\t\t\"stock\": 9999895,\n" +
                "\t\t\t\"date\": 1524621059000,\n" +
                "\t\t\t\"sort\": 999,\n" +
                "\t\t\t\"category\": 0,\n" +
                "\t\t\t\"type\": 1,\n" +
                "\t\t\t\"status\": 0,\n" +
                "\t\t\t\"handler\": {},\n" +
                "\t\t\t\"hibernateLazyInitializer\": {}\n" +
                "\t\t},\n" +
                "\t\t\"count\": 1,\n" +
                "\t\t\"barCode\": null,\n" +
                "\t\t\"washingEffect\": null,\n" +
                "\t\t\"flaw\": null,\n" +
                "\t\t\"remark\": null,\n" +
                "\t\t\"image\": null,\n" +
                "\t\t\"problemImage\": null,\n" +
                "\t\t\"status\": 0\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": \"6PgB42LJH12yG82vZsDT20A04\",\n" +
                "\t\t\"laundryProduct\": {\n" +
                "\t\t\t\"id\": \"AmLu1RmlFDaoX4-RKCpUj0A01\",\n" +
                "\t\t\t\"number\": \"524184400300\",\n" +
                "\t\t\t\"createactorid\": \"admin\",\n" +
                "\t\t\t\"updateactorid\": \"t9OTKRSsGKylVHZcevJtx1A02\",\n" +
                "\t\t\t\"updatetime\": 1524879711622,\n" +
                "\t\t\t\"createtime\": 1524184400300,\n" +
                "\t\t\t\"name\": \"皮鞋2\",\n" +
                "\t\t\t\"old_price\": 99999,\n" +
                "\t\t\t\"price\": 9999,\n" +
                "\t\t\t\"logo\": \"152471083812030026.jpg\",\n" +
                "\t\t\t\"stock\": 9966,\n" +
                "\t\t\t\"date\": 11111,\n" +
                "\t\t\t\"sort\": 10004,\n" +
                "\t\t\t\"category\": 4,\n" +
                "\t\t\t\"type\": 1,\n" +
                "\t\t\t\"status\": 0,\n" +
                "\t\t\t\"handler\": {},\n" +
                "\t\t\t\"hibernateLazyInitializer\": {}\n" +
                "\t\t},\n" +
                "\t\t\"count\": 1,\n" +
                "\t\t\"barCode\": null,\n" +
                "\t\t\"washingEffect\": null,\n" +
                "\t\t\"flaw\": null,\n" +
                "\t\t\"remark\": null,\n" +
                "\t\t\"image\": null,\n" +
                "\t\t\"problemImage\": null,\n" +
                "\t\t\"status\": 0\n" +
                "\t}\n" +
                "]";

        System.out.println(jsonStr);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, requestHeaders);
        String jsonData = restTemplate.postForObject(url, httpEntity, String.class);
        System.out.println(jsonData);
    }
}