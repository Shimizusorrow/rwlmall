package com.qunchuang.rwlmall;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.alibaba.fastjson.JSON;
import com.qunchuang.rwlmall.utils.MD5Util;
import com.qunchuang.rwlmall.utils.QiNiuUtil;
import com.qunchuang.rwlmall.utils.SFUtil;
import com.sf.csim.express.service.CallExpressServiceTools;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Curtain
 * @date 2018/3/27 8:44
 */

public class XMLText {

    @Test
    public void test(){
        Element request = new Element("Request");
        request.setAttribute("service","LaundryOrderService");
        request.setAttribute("lang","zh-CN");
        Element head = new Element("Head");
        head.addContent("7Xop9N5XKXgMY2LyByak33KOITVOkG6e");
        Element body = new Element("Body");
        Element order = new Element("LaundryOrder");
        Element addedService = new Element("AddedService");
        request.addContent(head);
        request.addContent(body);
        body.addContent(order);
        order.addContent(addedService);
        order.setAttribute("orderid","SFB-20170614001");
        order.setAttribute("express_type","2");
        order.setAttribute("j_province","广东省");
        order.setAttribute("j_city","深圳市");
        order.setAttribute("j_company","顺丰镖局");
        order.setAttribute("j_contact","丰哥");
        order.setAttribute("j_tel","15012345678");
        order.setAttribute("j_address","福田区新洲十一街万基商务大厦26楼");
        order.setAttribute("d_province","广东省");
        order.setAttribute("d_city","广州市");
        order.setAttribute("d_county","");
        order.setAttribute("d_company","神罗科技");
        order.setAttribute("d_contact","风一样的旭哥");
        order.setAttribute("d_tel","33992159");
        order.setAttribute("d_address","海珠区宝芝林大厦701室");
        order.setAttribute("parcel_quantity","1");
        order.setAttribute("pay_method","3");
        order.setAttribute("custid","7551234567");
        order.setAttribute("customs_batchs","");
        order.setAttribute("cargo","iphone 7 plus");

        addedService.setAttribute("name","COD");
        addedService.setAttribute("value","1.01");
        addedService.setAttribute("value1","7551234567");


        try{
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            XMLOutputter xmlOut = new XMLOutputter(format);
            String outString = xmlOut.outputString(request);
//            String resultStr = outString.substring(outString.indexOf("\n")+1);//用于截去第一段引用
            System.out.println(outString);


            HttpURLConnection connection = null;

            try {
                URL url = new URL("http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService");
                connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);//使用?URL?连接进行输出??
            connection.setDoInput(true);//使用?URL?连接进行输入??
            connection.setUseCaches(false);//忽略缓存??
                connection.setRequestMethod("POST");
                DataOutputStream bufOut = new DataOutputStream(connection.getOutputStream());
                Map data = new HashMap();
                data.put("orderId",outString);
                JSON.toJSONString(data);
                bufOut.write(outString.getBytes());

                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    System.out.println(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }

/*            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());

            HttpEntity<String> formEntity = new HttpEntity<String>(outString, headers);
            String repose = restTemplate.postForObject("http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService", formEntity, String.class);
            System.out.println(repose);*/

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        Element request = new Element("Request");
        request.setAttribute("service","LaundryOrderService");
        request.setAttribute("lang","zh-CN");
        Element head = new Element("Head");
        Element cargo = new Element("Cargo");
        head.addContent("SLKJ2019");
        Element body = new Element("Body");
        Element order = new Element("LaundryOrder");
//        Element addedService = new Element("AddedService");
        request.addContent(head);
        request.addContent(body);
        body.addContent(order);
        order.addContent(cargo);
//        order.addContent(addedService);
        order.setAttribute("orderid","SFB-20170614006");
        order.setAttribute("express_type","2");
        order.setAttribute("j_province","广东省");
        order.setAttribute("j_city","深圳市");
        order.setAttribute("j_company","顺丰镖局");
        order.setAttribute("j_contact","丰哥");
        order.setAttribute("j_tel","15012345678");
        order.setAttribute("j_address","福田区新洲十一街万基商务大厦26楼");
        order.setAttribute("d_province","广东省");
        order.setAttribute("d_city","广州市");
//        order.setAttribute("d_county","");
        order.setAttribute("d_company","神罗科技");
        order.setAttribute("d_contact","风一样的旭哥");
        order.setAttribute("d_tel","33992159");
        order.setAttribute("d_address","海珠区宝芝林大厦701室");
        order.setAttribute("parcel_quantity","1");
        order.setAttribute("pay_method","1");
        order.setAttribute("custid","9999999999");
        order.setAttribute("is_docall","1");
        cargo.setAttribute("name", "Dresses");
        cargo.setAttribute("count","1");
        cargo.setAttribute("unit","piece");
        cargo.setAttribute("weight","1.000");
        cargo.setAttribute("amount", "1");
        cargo.setAttribute("currency","USD");
        cargo.setAttribute("source_area","CN");
//        order.setAttribute("customs_batchs","");
//        order.setAttribute("cargo","iphone 7 plus");

//        addedService.setAttribute("name","COD");
//        addedService.setAttribute("value","1.01");
//        addedService.setAttribute("value1","7551234567");

        String reqXml = "";
        try {
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            XMLOutputter xmlOut = new XMLOutputter(format);
            String outString = xmlOut.outputString(request);
//            String resultStr = outString.substring(outString.indexOf("\n")+1);//用于截去第一段引用
            System.out.println(outString);
            reqXml = outString;
        }catch (Exception e){
            e.printStackTrace();
        }
        //丰桥平台公共测试账号
        //SLKJ2019
        //FBIqMkZjzxbsZgo7jTpeq7PD8CVzLT4Q
        String reqURL="http://218.17.248.244:11080/bsp-oisp/sfexpressService";
        String clientCode="MHKXD";//此处替换为您在丰桥平台获取的顾客编码
        String checkword="bf4DQLUfsJmgNVlq";//此处替换为您在丰桥平台获取的校验码
        CallExpressServiceTools client=CallExpressServiceTools.getInstance();
        String myReqXML=reqXml.replace("SLKJ2019",clientCode);
        System.out.println("请求报文："+myReqXML);
        String respXml= client.callSfExpressServiceByCSIM(reqURL, myReqXML, clientCode, checkword);

        if (respXml != null) {
            System.out.println("--------------------------------------");
            System.out.println("返回报文: "+ respXml);
            System.out.println("--------------------------------------");
        }

    }

    @Test
    public void test3(){


        Element request = new Element("Request");
        request.setAttribute("service","RouteService");
        request.setAttribute("lang","zh-CN");
        Element head = new Element("Head");
        head.addContent("SLKJ2019");
        Element body = new Element("Body");
        Element routeRequest = new Element("RouteRequest");
//        Element addedService = new Element("AddedService");
        request.addContent(head);
        request.addContent(body);
        body.addContent(routeRequest);
//        order.addContent(addedService);
        routeRequest.setAttribute("tracking_type","1");
        routeRequest.setAttribute("method_type","1");
        routeRequest.setAttribute("tracking_number","444034004105");


        String reqXml = "";
        try {
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            XMLOutputter xmlOut = new XMLOutputter(format);
            String outString = xmlOut.outputString(request);
//            String resultStr = outString.substring(outString.indexOf("\n")+1);//用于截去第一段引用
            System.out.println(outString);
            reqXml = outString;
        }catch (Exception e){
            e.printStackTrace();
        }


        //丰桥平台公共测试账号
        //SLKJ2019
        //FBIqMkZjzxbsZgo7jTpeq7PD8CVzLT4Q
        String reqURL="http://218.17.248.244:11080/bsp-oisp/sfexpressService";
        String clientCode="MHKXD";//此处替换为您在丰桥平台获取的顾客编码
        String checkword="bf4DQLUfsJmgNVlq";//此处替换为您在丰桥平台获取的校验码
        CallExpressServiceTools client=CallExpressServiceTools.getInstance();
        String myReqXML=reqXml.replace("SLKJ2019",clientCode);
        System.out.println("请求报文："+myReqXML);
        String respXml= client.callSfExpressServiceByCSIM(reqURL, myReqXML, clientCode, checkword);

        if (respXml != null) {
            System.out.println("--------------------------------------");
            System.out.println("返回报文: "+ respXml);
            System.out.println("--------------------------------------");
        }

    }

    @Test
    public void test4(){
        String xml = "<?xml version='1.0' encoding='UTF-8'?><Response service=\"RouteService\"><Head>OK</Head><Body>\n" +
                "<RouteResponse mailno=\"444000254878\" orderid=\"SFB-20170614001\">\n" +
                "<Route remark=\"已经收件\" accept_time=\"2017-08-31 02:01:44\" accept_address=\"广东省深圳市软件产业基地\" opcode=\"50\"/><Route remark=\"已经收件\" accept_time=\"2017-08-31 02:01:44\" accept_address=\"广东省深圳市软件产业基地\" opcode=\"80\"/></RouteResponse></Body></Response>\n";
        SFUtil.parseRoutingQueryXML(xml);
    }

    @Test
    public void test5(){
        char[] c = new char[10];
        LinkedList<Object> linkedList = new LinkedList<>();
        ArrayList<Object> arrayList = new ArrayList<>(1);
        System.out.println(arrayList);
        System.out.println(arrayList.size());
        arrayList.add(new Object());
        System.out.println(arrayList.size());
        arrayList.add(new Object());
        System.out.println(arrayList.size());
        arrayList.add(new Object());
        System.out.println(arrayList.size());
        System.out.println(c);
    }

    @Test
    public void test6(){
//        List<String> list = new ArrayList<>(Collections.nCopies(5,"Hello"));
//        System.out.println(list);
        int i = 10000;
        int k = 10000;
        System.out.println(i==k);
        System.out.println(new Integer(10000).equals(new Integer(10000)));
    }


    @Test
    public void test7(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        Integer k = null;
        for (Integer i: list){
            if (i==3){
                k=i;
            }
        }

        list.remove(k);
        list.add(0,k);

        System.out.println(list);
    }


    @Test
    public void test8(){
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2;

        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);

        list2 = list1.stream().filter(o -> (o < 3 || o==3)).collect(Collectors.toList());

        System.out.println(list2);
        System.out.println("-------------------");
        System.out.println(list1);

    }

    @Test
    public void test9(){
        List<Integer> list1= new ArrayList<>();
        List<Integer> list2= new ArrayList<>();
        List<Integer> list3= new ArrayList<>();
        list3.add(1);
        list1.addAll(list2);
        list1.addAll(list3);

        System.out.println(list1);
    }
    @Test
    public void test10(){

        HashMap<String, Integer> map = new HashMap<>();

        map.put("1",11);

        if (map.containsKey("1")){
            map.put(new Integer(1).toString(),map.get("1")+11);
        }else {
            map.put("1",map.get("1")+11);
        }

        System.out.println(map);


        t t = new t();
        t.setK(t.getK()+1);
        System.out.println(t.getK());
        t.setI(t.getI()+1);
        System.out.println(t.getI());
    }

    @Test
    public void test11(){
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("s1", 2);
        treeMap.put("w2", 5);
        treeMap.put("d3", 1);
        treeMap.put("f4", 0);
        treeMap.put("h5", 9);
        treeMap.put("q6", 22);
        treeMap.put("f7", 0);
        treeMap.put("h8", 9);
        treeMap.put("q9", 22);
        treeMap.put("a10", 25);
        treeMap.put("h82", 9);
        treeMap.put("q92", 22);
        treeMap.put("a120", 25);
        //按照value排序
        List<Map.Entry<String, Integer>> entryArrayList = new ArrayList<>(treeMap.entrySet());
        Collections.sort(entryArrayList,Collections.reverseOrder( Comparator.comparing(Map.Entry::getValue)));
        entryArrayList = entryArrayList.subList(0,3);
        System.out.println("----------------------*------------------------------");
        for (Map.Entry<String, Integer> entry : entryArrayList) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.println("----------------------*------------------------------");
//        entryArrayList.stream().
    }

    @Getter
    @Setter
    class t{
        private Integer i;
        private Integer k = 0;
    }

    @Test
    public void test12(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today=c.getTimeInMillis();
        System.out.println(today);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(today);
        int year = calendar2.get(Calendar.YEAR);
        int month = calendar2.get(Calendar.MONTH)+1;
        int day = calendar2.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar2.get(Calendar.HOUR_OF_DAY);//24小时制
//      int hour = calendar2.get(Calendar.HOUR);//12小时制
//        int minute = calendar2.get(Calendar.MINUTE);
//        int second = calendar2.get(Calendar.SECOND);
        System.out.println(year +"   "+month+"   "+day+"  ");
    }

    @Test
    public void test14(){
        String s1 = new String("瓯海区,鹿城区,龙湾区");
        String s2 = new String("");

        System.out.println(s1.contains(s2));


    }

    @Test
    public void test15(){
        long t0 = System.nanoTime();

        //初始化一个范围100万整数流,求能被2整除的数字，toArray（）是终点方法

        int a[]= IntStream.range(0, 1_000_0000).filter(p -> p % 2==0).toArray();

        long t1 = System.nanoTime();

        //和上面功能一样，这里是用并行流来计算

        int b[]=IntStream.range(0, 1_000_0000).parallel().filter(p -> p % 2==0).toArray();

        long t2 = System.nanoTime();

        //我本机的结果是serial: 0.06s, parallel 0.02s，证明并行流确实比顺序流快

        System.out.printf("serial: %.2fs, parallel %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9);


    }

    @Test
    public void test16(){

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("1","1");
        hashMap.put("2","1");
        hashMap.put("3","1");
        hashMap.put("4","2");
        hashMap.put("5","2");
        hashMap.put("6","3");
        hashMap.put("7","6");

        HashMap<Object, Object> hashMap2 = new HashMap<>();
        Set<Map.Entry<Object, Object>> entries = hashMap.entrySet();
        List<Object> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for(Map.Entry<Object, Object> map : entries){
            Object value = map.getValue();
            Object key = map.getKey();

            list1.add(value);
            list2.add(0);
            int i =0;
            if (hashMap2.containsKey(value)){
                for (Object o :list1){

                    if (value.equals(o)){
                        list2.set(i,list2.get(i)+1);
                        break;
                    }
                    i++;
                }
            }else {
                hashMap2.put(value,key);
            }
        }

        for (int i=0;i<list2.size();i++){
            if (list2.get(i)>0){
                System.out.println(list1.get(i)+":"+list2.get(i));
            }
        }
        list1.forEach(o-> System.out.println(o));
        list2.forEach(o-> System.out.println(o));

    }

    @Test
    public void test20(){
        System.out.println(LocalDate.now());
        String md5Str = MD5Util.getMD5Str("cid=1019208059cno=5770020135d=2018-06-26 01:43:57format=xmloper=adminshop=170sid=101sn=2018062500term=158v=1abcdefg0123456");
        System.out.println(md5Str);
    }


    @Test
    public void test21(){
//        Long i =10l;
//        System.out.println(i/100.0);

//        String s = "1,2,3,4";
//
//        String[] strings = s.split(",");
//
//
//
//        String string = strings[strings.length-1]; //获取  最后一个元素
//
//        String substring = s.substring(0, s.lastIndexOf(",")); //去掉最后一个， 及后面的字符
//        System.out.println(substring);



    }

}
