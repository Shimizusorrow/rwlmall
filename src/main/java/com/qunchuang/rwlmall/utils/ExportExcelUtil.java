package com.qunchuang.rwlmall.utils;

import com.csvreader.CsvWriter;
import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.domain.MallOrderItem;
import com.qunchuang.rwlmall.enums.MallAndFurnitureOrderStatusEnum;
import com.qunchuang.rwlmall.enums.PayStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.service.MallOrderService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExportExcelUtil {


    public static void returnFile(Integer status, HttpServletResponse response, String excelpath) {

        MallOrderService mallOrderService = (MallOrderService) SpringUtil.getBean("mallOrderServiceImpl");

        List<MallOrder> mallOrders;
        String fileName = "商城";

        if (MallAndFurnitureOrderStatusEnum.NEW.getCode().equals(status)) {
            fileName += "(" + MallAndFurnitureOrderStatusEnum.NEW.getMessage() + ")";
            mallOrders = mallOrderService.findByStatus(status).stream().filter(mallOrder -> mallOrder.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())).collect(Collectors.toList());
        } else if (MallAndFurnitureOrderStatusEnum.DELIVERY.getCode().equals(status)) {
            fileName += "(" + MallAndFurnitureOrderStatusEnum.DELIVERY.getMessage() + ")";
            mallOrders = mallOrderService.findByStatus(status);
        } else if (MallAndFurnitureOrderStatusEnum.SEND.getCode().equals(status)) {
            fileName += "(" + MallAndFurnitureOrderStatusEnum.SEND.getMessage() + ")";
            mallOrders = mallOrderService.findByStatus(status);
        } else if (MallAndFurnitureOrderStatusEnum.FINISHED.getCode().equals(status)) {
            fileName += "(" + MallAndFurnitureOrderStatusEnum.FINISHED.getMessage() + ")";
            mallOrders = mallOrderService.findByStatus(status);
        } else if (MallAndFurnitureOrderStatusEnum.CANCEL.getCode().equals(status)) {
            fileName += "(" + MallAndFurnitureOrderStatusEnum.CANCEL.getMessage() + ")";
            mallOrders = mallOrderService.findByStatus(status);
        } else {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR.getMessage());
        }

        try {
            //设置文件路径 文件名
            File directory = new File(excelpath);
            fileName += LocalDate.now();
            File tempFile = File.createTempFile(fileName, ".csv", directory);
            CsvWriter csvWriter = new CsvWriter(tempFile.getCanonicalPath(), ',', Charset.forName("GBK"));

            //将数据转化为excel
            String[] headers = {"订单号", "姓名", "地址", "联系电话", "备注", "商品名称", "商品数量", "商品规格", "商品现价"};
            csvWriter.writeRecord(headers);
            for (MallOrder mallOrder : mallOrders) {

                for (MallOrderItem mallOrderItem : mallOrder.getItems()) {

                    csvWriter.write(mallOrder.getNumber());
                    csvWriter.write(mallOrder.getName());
                    csvWriter.write(mallOrder.getProvince()+mallOrder.getCity()+mallOrder.getArea()+mallOrder.getAddress());
                    csvWriter.write(mallOrder.getPhone());
                    csvWriter.write(mallOrder.getRemark());
                    csvWriter.write(mallOrderItem.getMallProduct().getName());
                    csvWriter.write(mallOrderItem.getCount() + "");
                    csvWriter.write(mallOrderItem.getMallProduct().getStandard());
                    csvWriter.write(AmountUtil.fenToYuan(mallOrderItem.getMallProduct().getPrice()) + "元");
                    csvWriter.endRecord();
                }

            }
            csvWriter.close();

            //将数据以流的形式返回
            response.reset();
            response.setContentType("application/ms-excel");
            response.setCharacterEncoding("utf-8");
            fileName = fileName + ".csv";
            response.setHeader("Content-disposition", "attachment;filename= " + new String(fileName.getBytes("UTF8"), "ISO8859-1"));
            response.flushBuffer();
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempFile.getCanonicalPath()));
            int by;
            while ((by = bis.read()) != -1) {
                dos.write(by);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
