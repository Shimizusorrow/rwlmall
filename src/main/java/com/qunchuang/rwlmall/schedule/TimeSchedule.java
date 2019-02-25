package com.qunchuang.rwlmall.schedule;

import com.qunchuang.rwlmall.config.WeChatAccountConfig;
import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.*;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.DateUtil;
import com.qunchuang.rwlmall.utils.SpringUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/23 10:52
 */

@Component
public class TimeSchedule {

    /*超时订单设置*/
    @Scheduled(cron = "00 00 00 * * ?")
    public void updateLaundryOrderStatus() {

        LaundryOrderService orderService = (LaundryOrderService) SpringUtil.getBean("laundryOrderServiceImpl");

        //找到所有的入站订单   普通洗衣
        List<LaundryOrder> laundryOrders = orderService.findByTypeAndStatus(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.INBOUND.getCode());

        //todo  加上上挂的订单

        for (LaundryOrder order : laundryOrders) {
            //判断入站时间是否大于7天
            Long inboundTime = order.getInboundTime();
            if ((inboundTime < (System.currentTimeMillis() - 7 * DateUtil.DAY_TIME_STAMP)) && (LaundryOrderTimeEnum.NORMAL.getCode().equals(order.getTimeOut()))) {
                order.setTimeOut(LaundryOrderTimeEnum.TIME_OUT.getCode());
            }
        }

        orderService.saveAll(laundryOrders);


        //找到所有的入站订单   高端洗护
        laundryOrders = orderService.findByTypeAndStatus(OrderTypeEnum.HIGH_LAUNDRY.getCode(), OrderStatusEnum.INBOUND.getCode());

        for (LaundryOrder order : laundryOrders) {
            //判断入站时间是否大于7天
            Long inboundTime = order.getInboundTime();
            if ((inboundTime < (System.currentTimeMillis() - 7 * DateUtil.DAY_TIME_STAMP)) && (LaundryOrderTimeEnum.NORMAL.getCode().equals(order.getTimeOut()))) {
                order.setTimeOut(LaundryOrderTimeEnum.TIME_OUT.getCode());
            }
        }
        orderService.saveAll(laundryOrders);
    }

//    /*完结订单 取消超时*/
//    @Scheduled(cron = "00 00 00 * * ?")
//    public void cancelTimeOut(){
//        LaundryOrderService laundryOrderService = (LaundryOrderService) SpringUtil.getBean("laundryOrderServiceImpl");
//
//    }

    /*订单48小时自动完结 每3个小时执行一次*/
    @Scheduled(fixedRate = 1000 * 60 * 180)
    public void finishOrder() {

        LaundryOrderService laundryOrderService = (LaundryOrderService) SpringUtil.getBean("laundryOrderServiceImpl");
        MallOrderService mallOrderService = (MallOrderService) SpringUtil.getBean("mallOrderServiceImpl");
        FurnitureOrderService furnitureOrderService = (FurnitureOrderService) SpringUtil.getBean("furnitureOrderServiceImpl");


        //48小时毫秒数
        final long hours = 172800000L;

        final long currentTime = System.currentTimeMillis();


        //洗衣
        List<LaundryOrder> laundryOrderList = laundryOrderService.findByTypeAndStatus(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.GIVE_BACK.getCode());
        for (LaundryOrder laundryOrder : laundryOrderList) {
            //订单状态 = 送还     时间大于48小时
            if (currentTime > (laundryOrder.getUpdatetime() + hours)) {
                //因为是字符串  updatetime  所以不行
                laundryOrderService.finish(laundryOrder.getId());
            }
        }

        //高端洗护
        laundryOrderList = laundryOrderService.findByTypeAndStatus(OrderTypeEnum.HIGH_LAUNDRY.getCode(), OrderStatusEnum.GIVE_BACK.getCode());
        for (LaundryOrder laundryOrder : laundryOrderList) {
            //时间大于48小时
            if (currentTime > (laundryOrder.getUpdatetime() + hours)) {
                laundryOrderService.finish(laundryOrder.getId());
            }
        }

        //小让家居
        List<FurnitureOrder> furnitureOrderList = furnitureOrderService.findByStatus(MallAndFurnitureOrderStatusEnum.SEND.getCode());
        for (FurnitureOrder furnitureOrder : furnitureOrderList) {
            //时间大于48小时
            if (currentTime > (furnitureOrder.getUpdatetime() + hours)) {
                furnitureOrderService.finish(furnitureOrder.getId());
            }
        }

        //小让商城
        List<MallOrder> mallOrderList = mallOrderService.findByStatus(MallAndFurnitureOrderStatusEnum.SEND.getCode());
        for (MallOrder mallOrder : mallOrderList) {
            //时间大于48小时
            if (currentTime > (mallOrder.getUpdatetime() + hours)) {
                mallOrderService.finish(mallOrder.getId());
            }
        }

    }


    /*商品上架*/
    @Scheduled(cron = "00 00 00 * * ?")
    public void updateProductStatus() {
        LaundryProductService laundryProductService = (LaundryProductService) SpringUtil.getBean("laundryProductServiceImpl");
        MallProductService mallProductService = (MallProductService) SpringUtil.getBean("mallProductServiceImpl");
        FurnitureProductService furnitureProductService = (FurnitureProductService) SpringUtil.getBean("furnitureProductServiceImpl");

        /*查找未上架 和 上架日期为此刻之前的商品*/
        List<LaundryProduct> laundryProducts = laundryProductService.findByStatusAndDateBefore(ProductStatusEnum.DOWN.getCode(), System.currentTimeMillis());
        List<MallProduct> mallProducts = mallProductService.findByStatusAndDateBefore(ProductStatusEnum.DOWN.getCode(), System.currentTimeMillis());
        List<FurnitureProduct> furnitureProducts = furnitureProductService.findByStatusAndDateBefore(ProductStatusEnum.DOWN.getCode(), System.currentTimeMillis());

        /*商品上架*/
        laundryProducts.forEach(laundryProduct -> {
            laundryProduct.setStatus(ProductStatusEnum.UP.getCode());
            laundryProduct.setAutoStatus(false);
        });
        laundryProductService.save(laundryProducts);

        mallProducts.forEach(mallProduct -> {
            mallProduct.setStatus(ProductStatusEnum.UP.getCode());
            mallProduct.setAutoStatus(false);
        });
        mallProductService.save(mallProducts);

        furnitureProducts.forEach(furnitureProduct -> {
            furnitureProduct.setStatus(ProductStatusEnum.UP.getCode());
            furnitureProduct.setAutoStatus(false);
        });
        furnitureProductService.save(furnitureProducts);

    }


    /*自动取消1个小时内未付款订单   5分钟执行一次*/
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void autoCancel() {
        MallOrderService mallOrderService = (MallOrderService) SpringUtil.getBean("mallOrderServiceImpl");
        FurnitureOrderService furnitureOrderService = (FurnitureOrderService) SpringUtil.getBean("furnitureOrderServiceImpl");
        LaundryOrderService laundryOrderService = (LaundryOrderService) SpringUtil.getBean("laundryOrderServiceImpl");

        /*查找失效订单*/
        List<MallOrder> mallOrders = mallOrderService.findByTimeAndPayStatusAndStatus(System.currentTimeMillis() - 4000000, System.currentTimeMillis() - 3600000, PayStatusEnum.WAIT.getCode(), OrderStatusEnum.WAIT_PAY.getCode());
        List<LaundryOrder> laundryOrders = laundryOrderService.findByTimeAndPayStatusAndStatus(System.currentTimeMillis() - 4000000, System.currentTimeMillis() - 3600000, PayStatusEnum.WAIT.getCode(), OrderStatusEnum.WAIT_PAY.getCode());
        List<FurnitureOrder> furnitureOrders = furnitureOrderService.findByTimeAndPayStatusAndStatus(System.currentTimeMillis() - 4000000, System.currentTimeMillis() - 3600000, PayStatusEnum.WAIT.getCode(), OrderStatusEnum.WAIT_PAY.getCode());

        //取消订单
        mallOrders.forEach(mallOrder -> mallOrderService.cancel(mallOrder.getId()));
        laundryOrders.forEach(laundryOrder -> laundryOrderService.cancel(laundryOrder.getId()));
        furnitureOrders.forEach(furnitureOrder -> furnitureOrderService.cancel(furnitureOrder.getId()));
    }



}
