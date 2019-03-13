package com.qunchuang.rwlmall.utils;

import com.qunchuang.rwlmall.bean.ExpressInfo;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.sf.csim.express.service.CallExpressServiceTools;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/27 14:23
 */
public class SFUtil {

    /*发送请求*/
    public static String postRequest(Element request) {

        String reqXml;
        try {
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            XMLOutputter xmlOut = new XMLOutputter(format);
            reqXml = xmlOut.outputString(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new RwlMallException(ResultExceptionEnum.SF_DISPATCH_ORDER_FAILURE,"请求报文/n"+request.toString());
        }
        String reqURL = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";
        String clientCode = "5775742659";//此处替换为您在丰桥平台获取的顾客编码
        String checkWord = "grxyKzTsmxNmQxKqSqj4jE0tos5NELZ4";//此处替换为您在丰桥平台获取的校验码
        CallExpressServiceTools client = CallExpressServiceTools.getInstance();
        String myReqXML = reqXml.replace("SLKJ2019", clientCode);
        System.out.println("请求报文：" + myReqXML);
        String respXml = CallExpressServiceTools.callSfExpressServiceByCSIM(reqURL, myReqXML, clientCode, checkWord);
        return respXml;
    }

    /*解析下单结果xml*/
    public static void parseDispatchOrder(String xml) {
        SAXBuilder builder = new SAXBuilder();
        Document readDoc = null;
        Reader in = new StringReader(xml);  //要先转换成Reader才能装载的

        try {
            readDoc = builder.build(in);  //就是这句搞死人,我开始看的时候以为可以直接把xml字符串装载.
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element element = readDoc.getRootElement();
        Element head = element.getChild("Head");
        System.out.println(xml);
        if (!("OK".equals(head.getValue()))) {
            throw new RwlMallException(ResultExceptionEnum.SF_DISPATCH_ORDER_FAILURE, "返回结果/n" + xml);
        }
    }

    /*解析路由查询结果xml*/
    public static List<ExpressInfo> parseRoutingQueryXML(String xml) {
        SAXBuilder builder = new SAXBuilder();
        Document readDoc = null;
        Reader in = new StringReader(xml);  //要先转换成Reader才能装载的

        try {
            readDoc = builder.build(in);  //就是这句搞死人,我开始看的时候以为可以直接把xml字符串装载.
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element element = readDoc.getRootElement();
        Element head = element.getChild("Head");
        if (!("OK".equals(head.getValue()))) {
            throw new RwlMallException(ResultExceptionEnum.SF_ROUTING_QUERY_FAILURE, "返回结果：" + xml);
        }
        Element routeResponse = element.getChild("Body").getChild("RouteResponse");
        List list = routeResponse.getChildren("Route");
        List<ExpressInfo> result = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Element e = (Element) list.get(i);
            ExpressInfo expressInfo = new ExpressInfo();
            expressInfo.setTime(e.getAttributeValue("accept_time"));
            expressInfo.setState(e.getAttributeValue("remark"));
            expressInfo.setAddress(e.getAttributeValue("accept_address"));
            result.add(expressInfo);
        }
        return result;
    }

}
