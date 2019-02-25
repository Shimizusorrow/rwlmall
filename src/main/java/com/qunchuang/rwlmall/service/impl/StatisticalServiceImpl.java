package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.*;
import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.*;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.AmountUtil;
import com.qunchuang.rwlmall.utils.DateUtil;
import com.qunchuang.rwlmall.utils.OrderComparator;
import com.qunchuang.rwlmall.utils.StatisticalUtil;
import org.aspectj.weaver.ast.Or;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.resources.ga.LocaleNames_ga;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Curtain
 * @date 2018/4/23 14:49
 */

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Autowired
    private FurnitureOrderService furnitureOrderService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private LaundryProductService laundryProductService;

    @Autowired
    private MallProductService mallProductService;

    @Autowired
    private FurnitureProductService furnitureProductService;

    @Autowired
    private RechargeOrderService rechargeOrderService;

    @Autowired
    private RefundService refundService;


    @Override
    public Map<String, Object> storeDayAnalysis(String storeId, Integer page) {

        Store store = storeService.findOne(storeId);

        /*第一条数据为当天开始时间凌晨*/
        Long startTime = DateUtil.getDateTimeStamp();

        /*结束时间为调用时间*/
        Long endTime = System.currentTimeMillis();

        /*门店创建时间*/
        long storeCreateTime = store.getCreatetime();

        /*总记录数*/
        long count = (endTime - storeCreateTime) / DateUtil.DAY_TIME_STAMP;

        if (page != 0) {
            startTime = DateUtil.getDateTimeStamp() - (DateUtil.DAY_TIME_STAMP * 10 * page);
            endTime = DateUtil.getDateTimeStamp() - (DateUtil.DAY_TIME_STAMP * 9 * page);
        }

        List<StoreDayStatistical> storeDayStatisticalList = new ArrayList<>();

        /*但查询时间大于门店创建时间 并且记录数小于30条时执行*/
        while (endTime > storeCreateTime && storeDayStatisticalList.size() < 10) {
            /*每日门店订单统计*/
            StoreDayStatistical storeDayStatistical = new StoreDayStatistical();

            /*订单总数*/
            Integer orderTotal = 0;

            /*商品数量*/
            Integer productTotal = 0;

            /*撤单数量*/
            Integer cancelOrderTotal = 0;

            /*商城收入*/
            Long mallTotal = 0L;

             /*洗衣*/
            Long laundryTotal = 0L;

            /*高端洗护*/
            Long highLaundryTotal = 0L;

            /*家具*/
            Long furnitureTotal = 0L;

            /*微信支付*/
            Long weChatPay = 0L;

            /*余额支付*/
            Long balancePay = 0L;

            /*卡支付*/
            Long cardPay = 0L;

            /*总收入*/
            Long incomeTotal = 0L;

            /*洗衣*/
            List<LaundryOrder> laundryOrders = laundryOrderService.findByStoreIdAndFinishTimeAndStatusAndType(storeId, startTime, endTime, OrderStatusEnum.FINISHED.getCode(), OrderTypeEnum.LAUNDRY.getCode());
            for (LaundryOrder order : laundryOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                laundryTotal += order.getAmount();
                if (order.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                    weChatPay += order.getAmount();
                } else if (order.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                    cardPay += order.getAmount();
                } else {
                    balancePay += order.getAmount();
                }
            }

            /*高端洗护*/
            List<LaundryOrder> highLaundryOrders = laundryOrderService.findByStoreIdAndFinishTimeAndStatusAndType(storeId, startTime, endTime, OrderStatusEnum.FINISHED.getCode(), OrderTypeEnum.HIGH_LAUNDRY.getCode());
            for (LaundryOrder order : highLaundryOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                highLaundryTotal += order.getAmount();
                if (order.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                    weChatPay += order.getAmount();
                } else if (order.getPayStatus().equals(PayModeEnum.CARD.getCode())) {
                    cardPay += order.getAmount();
                } else {
                    balancePay += order.getAmount();
                }
            }

            /*家居*/
            List<FurnitureOrder> furnitureOrders = furnitureOrderService.findByStoreIdAndFinishTimeAndStatus(storeId, startTime, endTime, MallAndFurnitureOrderStatusEnum.FINISHED.getCode());
            for (FurnitureOrder order : furnitureOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                furnitureTotal += order.getAmount();
                if (order.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                    weChatPay += order.getAmount();
                } else if (order.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                    cardPay += order.getAmount();
                } else {
                    balancePay += order.getAmount();
                }
            }

            /*商城*/
            List<MallOrder> mallOrders = mallOrderService.findByStoreIdAndFinishTimeAndStatus(storeId, startTime, endTime, MallAndFurnitureOrderStatusEnum.FINISHED.getCode());
            for (MallOrder order : mallOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                mallTotal += order.getAmount();
                if (order.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                    weChatPay += order.getAmount();
                } else if (order.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                    cardPay += order.getAmount();
                } else {
                    balancePay += order.getAmount();
                }

            }

            /*设置门店每天的数据*/
            incomeTotal = laundryTotal + highLaundryTotal + furnitureTotal + mallTotal;
            storeDayStatistical.setDate(DateUtil.getDate(startTime));
            storeDayStatistical.setBalancePay(balancePay);
            storeDayStatistical.setFurnitureTotal(furnitureTotal);
            storeDayStatistical.setHighLaundryTotal(highLaundryTotal);
            storeDayStatistical.setLaundryTotal(laundryTotal);
            storeDayStatistical.setMallTotal(mallTotal);
            storeDayStatistical.setOrderTotal(orderTotal);
            storeDayStatistical.setIncomeTotal(incomeTotal);
            storeDayStatistical.setProductTotal(productTotal);
            storeDayStatistical.setWeChatPay(weChatPay);
            storeDayStatistical.setCardPay(cardPay);

            Map<String, Long> incomePercentage = incomePercentage(laundryTotal, highLaundryTotal, furnitureTotal, mallTotal);
            storeDayStatistical.setAgentIncome(incomePercentage.get("agentIncome"));
            storeDayStatistical.setPlatformIncome(incomePercentage.get("platformIncome"));
            storeDayStatistical.setExpressIncome(incomePercentage.get("expressIncome"));

            storeDayStatisticalList.add(storeDayStatistical);

            /*时间向前一天*/
            endTime = startTime;
            startTime = startTime - DateUtil.DAY_TIME_STAMP;
        }

        /*结果集*/
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("count", count);
        map.put("content", storeDayStatisticalList);
        return map;
    }

    @Override
    public List<StoreStatistical> storeAnalysis(Long startTime, Long endTime, String province, String city, String area, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*结果集*/
        List<StoreStatistical> result = new ArrayList<>();

        /*查找区域中的门店*/
        List<Store> stores;

        /*区别代理商  和 总部 看到的门店*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            /*查找代理商+区域中的门店*/
            stores = storeService.findByAgentIdAndProvinceContainingAndCityContainingAndAreaContaining(role.getAccountId(), province, city, area);

        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            /*查找区域中的门店*/
            stores = storeService.findByProvinceContainingAndCityContainingAndAreaContaining(province, city, area);
        } else {
            throw new AccessDeniedException("权限不足");
        }


        /*遍历所有门店*/
        for (Store store : stores) {
            StoreStatistical storeStatistical = new StoreStatistical();
            /*订单总数*/
            Integer orderTotal = 0;

            /*商品数量*/
            Integer productTotal = 0;

            /*商城收入*/
            Long mallTotal = 0L;

             /*洗衣*/
            Long laundryTotal = 0L;

            /*高端洗护*/
            Long highLaundryTotal = 0L;

            /*家具*/
            Long furnitureTotal = 0L;

            /*总收入*/
            Long incomeTotal = 0L;



            /*按门店和时间查找订单*/
            //普通洗衣
            List<LaundryOrder> orders = laundryOrderService.findByStoreIdAndFinishTimeAndStatusAndType(store.getId(), startTime, endTime, OrderStatusEnum.FINISHED.getCode(), OrderTypeEnum.LAUNDRY.getCode());
            for (LaundryOrder order : orders) {
                orderTotal++;
                productTotal += order.getItems().size();
                laundryTotal += order.getAmount();
            }

            List<LaundryOrder> highOrders = laundryOrderService.findByStoreIdAndFinishTimeAndStatusAndType(store.getId(), startTime, endTime, OrderStatusEnum.FINISHED.getCode(), OrderTypeEnum.HIGH_LAUNDRY.getCode());
            for (LaundryOrder order : highOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                highLaundryTotal += order.getAmount();
            }

            List<FurnitureOrder> furnitureOrders = furnitureOrderService.findByStoreIdAndFinishTimeAndStatus(store.getId(), startTime, endTime, OrderStatusEnum.FINISHED.getCode());
            for (FurnitureOrder order : furnitureOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                furnitureTotal += order.getAmount();
            }

            List<MallOrder> mallOrders = mallOrderService.findByStoreIdAndFinishTimeAndStatus(store.getId(), startTime, endTime, OrderStatusEnum.FINISHED.getCode());
            for (MallOrder order : mallOrders) {
                orderTotal++;
                productTotal += order.getItems().size();
                mallTotal += order.getAmount();

            }

            //设置每个门店的统计数据
            storeStatistical.setName(store.getName());
            storeStatistical.setFurnitureTotal(furnitureTotal);
            storeStatistical.setHighLaundryTotal(highLaundryTotal);
            storeStatistical.setLaundryTotal(laundryTotal);
            storeStatistical.setIncomeTotal(laundryTotal + highLaundryTotal + furnitureTotal + mallTotal);
            storeStatistical.setMallTotal(mallTotal);
            storeStatistical.setOrderTotal(orderTotal);
            storeStatistical.setProductTotal(productTotal);
            storeStatistical.setRechargeTotal(0L);
            storeStatistical.setRefundTotal(0L);

            Map<String, Long> incomePercentage = incomePercentage(laundryTotal, highLaundryTotal, furnitureTotal, mallTotal);

            storeStatistical.setAgentIncome(incomePercentage.get("agentIncome"));
            storeStatistical.setPlatformIncome(incomePercentage.get("platformIncome"));
            storeStatistical.setExpressIncome(incomePercentage.get("expressIncome"));

            storeStatistical.setName(store.getName());
            storeStatistical.setId(store.getId());

            result.add(storeStatistical);
        }

        return result;
    }

    @Override
    public Map<String, List> mallConsumeStatistical(Long startTime, Long endTime, String province, String city, String area) {
          /*总结果集*/
        Map<String, List> result;

        /*用户消费次数结果集*/
        List<Integer> consumeFrequencyList = new ArrayList<>();

         /*用户消费金额区间结果集*/
        List<Integer> consumePriceList = new ArrayList<>();

        /*商品结果统计*/
        List<ProductStatistical> productStatisticalList = new ArrayList<>();

        /*衣服销售统计结果统计*/
        TreeMap<String, Integer> productStatisticalMap = new TreeMap<>();

        /*用户消费结果统计*/
        List<ConsumeStatistical> consumeStatisticalList = new ArrayList<>();

        //查找所有符合条件消费订单
        List<MallOrder> orders = mallOrderService.findByStatusNotAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

        //遍历订单
        for (MallOrder order : orders) {

            //判断结果集中是否有此用户的消费信息
            String userId = order.getUserId();
            ConsumeStatistical consumeStatistical = null;

            //遍历用户消费集判断用户是否已经存在
            for (ConsumeStatistical statistical : consumeStatisticalList) {
                if (statistical.getUserId().equals(userId)) {
                    consumeStatistical = statistical;
                    break;
                }
            }

            if (consumeStatistical != null) {
                //存在  重新修改值
                consumeStatistical.setCount(consumeStatistical.getCount() + 1);
                consumeStatistical.setMoney(consumeStatistical.getMoney() + order.getAmount());
            } else {
                //不存在则 新建一个 并加入到集合中
                consumeStatistical = new ConsumeStatistical();
                consumeStatistical.setCount(1);
                consumeStatistical.setUserId(userId);
                consumeStatistical.setMoney(order.getAmount());
                consumeStatisticalList.add(consumeStatistical);
            }

            //订单购物车中的商品增加消费件数
            for (MallOrderItem mallOrderItem : order.getItems()) {
                String name = mallOrderItem.getMallProduct().getName();

                if (productStatisticalMap.containsKey(name)) {
                    //如果已经存在 则加对应的件数
                    productStatisticalMap.put(name, productStatisticalMap.get(name) + mallOrderItem.getCount());

                } else {
                    //不存在 创建 并加件数
                    productStatisticalMap.put(name, mallOrderItem.getCount());
                }
            }

        }

        //对结果进行统计，与填充到总结果集
        result = userConsumeStatistical(productStatisticalMap, productStatisticalList, consumeFrequencyList, consumePriceList, consumeStatisticalList);

        return result;
    }

    @Override
    public Map<String, Object> furnitureProductStatistical(Long startTime, Long endTime, String province, String city, String area) {
        /*总结果集*/
        Map<String, Object> result = new HashMap<>(2);

        /*商品结果集*/
        List<ProductStatistical> productStatisticalList = new ArrayList<>();

        /*总件数*/
        Integer total = 0;

        /*商品销售统计结果统计*/
        TreeMap<String, ProductStatistical> productStatisticalMap = new TreeMap<>();


        /*获取所有商品*/
        List<FurnitureProduct> furnitureProductList = furnitureProductService.findAll();

        /*添加商品到集合*/
        for (FurnitureProduct furnitureProduct : furnitureProductList) {
            ProductStatistical productStatistical = new ProductStatistical();
            productStatistical.setCount(0);
            productStatistical.setProductName(furnitureProduct.getName());
            productStatisticalMap.put(furnitureProduct.getName(), productStatistical);
        }

        //查找所有符合条件消费订单
        List<FurnitureOrder> orders = furnitureOrderService.findByStatusNotAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

        //遍历订单
        for (FurnitureOrder order : orders) {
            //订单购物车中的商品增加消费件数
            for (FurnitureOrderItem furnitureOrderItem : order.getItems()) {

                /*key*/
                String name = furnitureOrderItem.getFurnitureProduct().getName();

                /*value*/
                ProductStatistical productStatistical = productStatisticalMap.get(name);

                //商品消费次数增加
                productStatistical.setCount(productStatistical.getCount() + furnitureOrderItem.getCount());

                //总次数增加
                total += furnitureOrderItem.getCount();

            }
        }

        /*填充结果集*/
        productStatisticalMap.entrySet().forEach(map -> productStatisticalList.add(map.getValue()));

        result.put("content", productStatisticalList);
        result.put("total", total);

        return result;
    }

    @Override
    public FinanceStatistical financeStatistical(Long startTime, Long endTime) {

        /*微信总收入*/
        Long weChatIncome = 0L;

        /*充值总金额*/
        Long rechargeIncome = 0L;

        /*订单微信支付总额*/
        Long weChatAmount = 0L;

        /*账户总余额*/
        Long accountBalance = 0L;

        /*退款总额*/
        Long refundAmount = 0L;

        /*充值奖励总额*/
        Long rewardAmount = 0L;

        /*余额消费总额*/
        Long balanceAmount = 0L;

        /*消费总额*/
        Long consumeAmount = 0L;

        /*会员卡消费总额*/
        Long cardAmount = 0L;

        /*微信支付总额*/
        ArrayList<Order> orders = new ArrayList<>();
        List<LaundryOrder> laundryOrderList = laundryOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.WE_CHAT.getCode());
        List<MallOrder> mallOrderList = mallOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.WE_CHAT.getCode());
        List<FurnitureOrder> furnitureOrderList = furnitureOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.WE_CHAT.getCode());

        orders.addAll(laundryOrderList);
        orders.addAll(mallOrderList);
        orders.addAll(furnitureOrderList);

        for (Order order : orders) {
            weChatAmount += order.getAmount();
        }

        /*余额支付总金额*/
        orders = new ArrayList<>();
        laundryOrderList = laundryOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.BALANCE.getCode());
        mallOrderList = mallOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.BALANCE.getCode());
        furnitureOrderList = furnitureOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.BALANCE.getCode());

        orders.addAll(laundryOrderList);
        orders.addAll(mallOrderList);
        orders.addAll(furnitureOrderList);

        for (Order order : orders) {
            balanceAmount += order.getAmount();
        }

        /*卡支付总金额*/
        orders = new ArrayList<>();
        laundryOrderList = laundryOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.CARD.getCode());
        mallOrderList = mallOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.CARD.getCode());
        furnitureOrderList = furnitureOrderService.findByTimeAndPayStatusAndPayMode(startTime, endTime, PayStatusEnum.SUCCESS.getCode(), PayModeEnum.CARD.getCode());

        orders.addAll(laundryOrderList);
        orders.addAll(mallOrderList);
        orders.addAll(furnitureOrderList);

        for (Order order : orders) {
            //取消卡折扣 计算后   直接获取订单价格
//            cardAmount += order.getCardAmount();
            cardAmount += order.getAmount();
        }



        /*消费总额*/
        consumeAmount = weChatAmount + balanceAmount + cardAmount;

        /*充值金额 充值奖励金额*/
        List<RechargeOrder> rechargeOrderList = rechargeOrderService.findByTimeAndPayStatus(startTime, endTime, PayStatusEnum.SUCCESS.getCode());

        for (RechargeOrder rechargeOrder : rechargeOrderList) {
            rechargeIncome += rechargeOrder.getPayMoney();
            rewardAmount += rechargeOrder.getRewardMoney();
        }

        /*退款总额*/
        List<Refund> refundList = refundService.findByTime(startTime, endTime);

        for (Refund refund : refundList) {
            refundAmount += refund.getMoney();
        }
        /*账户总余额*/
        List<User> userList = userService.findAll();
        for (User user : userList) {
            accountBalance += user.getBalance();
        }

        /*微信总收入*/
        weChatIncome = rechargeIncome + weChatAmount;


        FinanceStatistical financeStatistical = new FinanceStatistical(weChatIncome, rechargeIncome, weChatAmount, accountBalance, refundAmount, rewardAmount, balanceAmount, consumeAmount, cardAmount);

        return financeStatistical;
    }

    @Override
    public Map<String, Object> mallProductStatistical(Long startTime, Long endTime, String province, String city, String area) {
       /*总结果集*/
        Map<String, Object> result = new HashMap<>(2);

        /*商品结果集*/
        List<ProductStatistical> productStatisticalList = new ArrayList<>();

        /*总件数*/
        Integer total = 0;

        /*商品销售统计结果统计*/
        TreeMap<String, ProductStatistical> productStatisticalMap = new TreeMap<>();


        /*获取所有商品*/
        List<MallProduct> mallOrderList = mallProductService.findAll();

        /*添加商品到集合*/
        for (MallProduct mallProduct : mallOrderList) {
            ProductStatistical productStatistical = new ProductStatistical();
            productStatistical.setCount(0);
            productStatistical.setCategory(mallProduct.getCategory());
            productStatistical.setProductName(mallProduct.getName());
            productStatisticalMap.put(mallProduct.getName(), productStatistical);
        }

        //查找所有符合条件消费订单
        List<MallOrder> orders = mallOrderService.findByStatusNotAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

        //遍历订单
        for (MallOrder order : orders) {
            //订单购物车中的商品增加消费件数
            for (MallOrderItem mallOrderItem : order.getItems()) {

                /*key*/
                String name = mallOrderItem.getMallProduct().getName();

                /*value*/
                ProductStatistical productStatistical = productStatisticalMap.get(name);

                //商品消费次数增加
                productStatistical.setCount(productStatistical.getCount() + mallOrderItem.getCount());

                //总次数增加
                total += mallOrderItem.getCount();

            }
        }

         /*填充结果集*/
        productStatisticalMap.entrySet().forEach(map -> productStatisticalList.add(map.getValue()));

        result.put("content", productStatisticalList);
        result.put("total", total);

        return result;
    }

    @Override
    public Map<String, Object> laundryProductStatistical(Long startTime, Long endTime, String province, String city, String area, Integer type) {
        /*总结果集*/
        Map<String, Object> result = new HashMap<>(2);

        /*商品结果集*/
        List<ProductStatistical> productStatisticalList = new ArrayList<>();

        /*总件数*/
        Integer total = 0;

        /*商品销售统计结果统计*/
        TreeMap<String, ProductStatistical> productStatisticalMap = new TreeMap<>();


        /*获取所有商品*/
        List<LaundryProduct> laundryProductList = laundryProductService.findByType(type);

        /*添加商品到集合*/
        for (LaundryProduct laundryProduct : laundryProductList) {
            ProductStatistical productStatistical = new ProductStatistical();
            productStatistical.setCount(0);
            productStatistical.setCategory(laundryProduct.getCategory());
            productStatistical.setProductName(laundryProduct.getName());
            productStatisticalMap.put(laundryProduct.getName(), productStatistical);
        }

        //查找所有符合条件消费订单
        List<LaundryOrder> orders = laundryOrderService.findByTypeAndStatusNotAndTimeAndArea(type, OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

        //遍历订单
        for (LaundryOrder order : orders) {
            //订单购物车中的商品增加消费件数
            for (LaundryOrderItem laundryOrderItem : order.getItems()) {

                /*key*/
                String name = laundryOrderItem.getLaundryProduct().getName();

                /*value*/
                ProductStatistical productStatistical = productStatisticalMap.get(name);

                //商品消费次数增加
                productStatistical.setCount(productStatistical.getCount() + laundryOrderItem.getCount());

                //总次数增加
                total += laundryOrderItem.getCount();

            }
        }

        /*按类别填充结果集*/
        productStatisticalMap.entrySet().forEach(map -> productStatisticalList.add(map.getValue()));

        result.put("content", productStatisticalList);
        result.put("total", total);

        return result;
    }

    @Override
    public Map<String, List> furnitureConsumeStatistical(Long startTime, Long endTime, String province, String city, String area) {
        /*总结果集*/
        Map<String, List> result;

        /*用户消费次数结果集*/
        List<Integer> consumeFrequencyList = new ArrayList<>();

         /*用户消费金额区间结果集*/
        List<Integer> consumePriceList = new ArrayList<>();

        /*商品结果统计*/
        List<ProductStatistical> productStatisticalList = new ArrayList<>();

        /*衣服销售统计结果统计*/
        TreeMap<String, Integer> productStatisticalMap = new TreeMap<>();

        /*用户消费结果统计*/
        List<ConsumeStatistical> consumeStatisticalList = new ArrayList<>();

        //查找所有符合条件消费订单
        List<FurnitureOrder> orders = furnitureOrderService.findByStatusNotAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

        //遍历订单
        for (FurnitureOrder order : orders) {

            //判断结果集中是否有此用户的消费信息
            String userId = order.getUserId();
            ConsumeStatistical consumeStatistical = null;

            //遍历用户消费集判断用户是否已经存在
            for (ConsumeStatistical statistical : consumeStatisticalList) {
                if (statistical.getUserId().equals(userId)) {
                    consumeStatistical = statistical;
                    break;
                }
            }

            if (consumeStatistical != null) {
                //存在  重新修改值
                consumeStatistical.setCount(consumeStatistical.getCount() + 1);
                consumeStatistical.setMoney(consumeStatistical.getMoney() + order.getAmount());
            } else {
                //不存在则 新建一个 并加入到集合中
                consumeStatistical = new ConsumeStatistical();
                consumeStatistical.setCount(1);
                consumeStatistical.setUserId(userId);
                consumeStatistical.setMoney(order.getAmount());
                consumeStatisticalList.add(consumeStatistical);
            }

            //订单购物车中的商品增加消费件数
            for (FurnitureOrderItem furnitureOrderItem : order.getItems()) {
                String name = furnitureOrderItem.getFurnitureProduct().getName();

                if (productStatisticalMap.containsKey(name)) {
                    //如果已经存在 则加对应的件数
                    productStatisticalMap.put(name, productStatisticalMap.get(name) + furnitureOrderItem.getCount());

                } else {
                    //不存在 创建 并加件数
                    productStatisticalMap.put(name, furnitureOrderItem.getCount());
                }
            }

        }

        //对结果进行统计，与填充到总结果集
        result = userConsumeStatistical(productStatisticalMap, productStatisticalList, consumeFrequencyList, consumePriceList, consumeStatisticalList);

        return result;
    }

    private Map<String, List> userConsumeStatistical(TreeMap<String, Integer> productStatisticalMap, List<ProductStatistical> productStatisticalList, List<Integer> consumeFrequencyList, List<Integer> consumePriceList, List<ConsumeStatistical> consumeStatisticalList) {

        /*商品Top结果集*/
        List<Map.Entry<String, Integer>> productStatisticalEntryList = new ArrayList<>(productStatisticalMap.entrySet());

        productStatisticalEntryList.sort(Collections.reverseOrder(Comparator.comparing(Map.Entry::getValue)));

        if (productStatisticalEntryList.size() > 10) {
            productStatisticalEntryList = productStatisticalEntryList.subList(0, 10);
        }

        //将结果添加上商品统计结果集
        for (Map.Entry<String, Integer> map : productStatisticalEntryList) {
            ProductStatistical productStatistical = new ProductStatistical();
            productStatistical.setProductName(map.getKey());
            productStatistical.setCount(map.getValue());
            productStatisticalList.add(productStatistical);
        }


        //初始化消费次数、消费价位结果集
        StatisticalUtil.initConsumeResultData(consumeFrequencyList, consumePriceList);

        //遍历用户消费结果统计
        for (ConsumeStatistical consumeStatistical : consumeStatisticalList) {
            Long money = consumeStatistical.getMoney();
            Integer frequency = consumeStatistical.getCount();

            //根据消费金额更新结果集
            int i = 0;
            for (; i < 10; i++) {
                if (money < StatisticalUtil.MONEY_INTERVAL[i]) {
                    consumePriceList.set(i, consumePriceList.get(i) + 1);
                    break;
                }
            }
            if (i == 10) {
                consumePriceList.set(i, consumePriceList.get(i) + 1);
            }

            //根据消费次数  更新记录
            if (frequency <= 10) {
                //1~10之间
                consumeFrequencyList.set(frequency, consumeFrequencyList.get(frequency) + 1);
            } else {
                //10以上
                consumeFrequencyList.set(11, consumeFrequencyList.get(11) + 1);
            }
        }
        //顾客消费0次的情况
        consumeFrequencyList.set(0, userService.count() - consumeStatisticalList.size());

         /*总结果集*/
        Map<String, List> result = new HashMap<>(3);

        //放入总结果集
        result.put("productTop", productStatisticalList);
        result.put("consumeFrequency", consumeFrequencyList);
        result.put("consumePrice", consumePriceList);

        return result;
    }

    @Override
    public Map<String, List> laundryConsumeStatistical(Long startTime, Long endTime, String province, String city, String area, Integer type) {
        /*总结果集*/
        Map<String, List> result;

        /*用户消费次数结果集*/
        List<Integer> consumeFrequencyList = new ArrayList<>();

         /*用户消费金额区间结果集*/
        List<Integer> consumePriceList = new ArrayList<>();

        /*商品结果统计*/
        List<ProductStatistical> productStatisticalList = new ArrayList<>();

        /*衣服销售统计结果统计*/
        TreeMap<String, Integer> productStatisticalMap = new TreeMap<>();

        /*用户消费结果统计*/
        List<ConsumeStatistical> consumeStatisticalList = new ArrayList<>();

        //查找所有符合条件消费订单
        List<LaundryOrder> orders = laundryOrderService.findByTypeAndStatusNotAndTimeAndArea(type, OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

        //遍历订单
        for (LaundryOrder order : orders) {

            //判断结果集中是否有此用户的消费信息
            String userId = order.getUserId();
            ConsumeStatistical consumeStatistical = null;

            //遍历用户消费集判断用户是否已经存在
            for (ConsumeStatistical statistical : consumeStatisticalList) {
                if (statistical.getUserId().equals(userId)) {
                    consumeStatistical = statistical;
                    break;
                }
            }

            if (consumeStatistical != null) {
                //存在  重新修改值
                consumeStatistical.setCount(consumeStatistical.getCount() + 1);
                consumeStatistical.setMoney(consumeStatistical.getMoney() + order.getAmount());
            } else {
                //不存在则 新建一个 并加入到集合中
                consumeStatistical = new ConsumeStatistical();
                consumeStatistical.setCount(1);
                consumeStatistical.setUserId(userId);
                consumeStatistical.setMoney(order.getAmount());
                consumeStatisticalList.add(consumeStatistical);
            }

            //订单购物车中的商品增加消费件数
            for (LaundryOrderItem laundryOrderItem : order.getItems()) {
                String name = laundryOrderItem.getLaundryProduct().getName();

                if (productStatisticalMap.containsKey(name)) {
                    //如果已经存在 则加对应的件数
                    productStatisticalMap.put(name, productStatisticalMap.get(name) + laundryOrderItem.getCount());

                } else {
                    //不存在 创建 并加件数
                    productStatisticalMap.put(name, laundryOrderItem.getCount());
                }
            }

        }

        //对结果进行统计，与填充到总结果集
        result = userConsumeStatistical(productStatisticalMap, productStatisticalList, consumeFrequencyList, consumePriceList, consumeStatisticalList);

        return result;
    }

    @Override
    public List<Object> allStoreOrder(String storeId, Integer type) {
        List<Object> result = new ArrayList<>();

        switch (type) {
            case 1:
                //1.添加所有未入站订单
                result.addAll(allInboundStoreOrder(storeId));
                break;
            case 2:
                //2.添加所有入站订单
                result.addAll(allHangOnStoreOrder(storeId));
                /*添加家居入站订单*/
                result.addAll(furnitureOrderService.findByStoreAndStatus(storeId, MallAndFurnitureOrderStatusEnum.SEND.getCode()));
                /*添加商城入站订单*/
                result.addAll(mallOrderService.findByStoreAndStatus(storeId, MallAndFurnitureOrderStatusEnum.SEND.getCode()));
                break;
            case 3:
                //3.添加所有上挂订单
                 /*添加普通洗衣订单*/
                result.addAll(laundryOrderService.findByStoreAndStatusAndType(storeId, OrderStatusEnum.HANG_ON.getCode(), OrderTypeEnum.LAUNDRY.getCode()));
                /*添加高端洗衣订单*/
                result.addAll(laundryOrderService.findByStoreAndStatusAndType(storeId, OrderStatusEnum.HANG_ON.getCode(), OrderTypeEnum.HIGH_LAUNDRY.getCode()));
                break;

            default:
                break;
        }


        return result;
    }

    @Override
    public List<LaundryOrder> allHangOnStoreOrder(String storeId) {

        List<LaundryOrder> result = new ArrayList<>();

        /*添加普通洗衣订单*/
        result.addAll(laundryOrderService.findByStoreAndStatusAndType(storeId, OrderStatusEnum.INBOUND.getCode(), OrderTypeEnum.LAUNDRY.getCode()));

        /*添加高端洗衣订单*/
        result.addAll(laundryOrderService.findByStoreAndStatusAndType(storeId, OrderStatusEnum.INBOUND.getCode(), OrderTypeEnum.HIGH_LAUNDRY.getCode()));

        /*排序*/
        result.sort(new OrderComparator());

        return result;
    }


    @Override
    public List<Order> storeFindNewOrder(String storeId, String key, Integer type) {
        /*结果集*/
        List<Order> orderList;

        /*获取门店所有新订单*/
        List<Order> orders = allInboundStoreOrder(storeId);

        if (QueryTypeEnum.COMMON_PROBLEM.getCode().equals(type)) {
            /*手机号查询*/
            orderList = orders.stream().filter(order -> order.getPhone().contains(key)).collect(Collectors.toList());
        } else {
            orderList = orders.stream().filter(order -> order.getNumber().equals(key)).collect(Collectors.toList());
        }

        return orderList;
    }

    @Override
    public List<Order> allInboundStoreOrder(String storeId) {

        List<Order> result = new ArrayList<>();

        /*添加普通洗衣订单*/
        result.addAll(laundryOrderService.findByStoreAndStatusAndType(storeId, OrderStatusEnum.DELIVERY.getCode(), OrderTypeEnum.LAUNDRY.getCode()));

        /*添加高端洗衣订单*/
        result.addAll(laundryOrderService.findByStoreAndStatusAndType(storeId, OrderStatusEnum.DELIVERY.getCode(), OrderTypeEnum.HIGH_LAUNDRY.getCode()));

        /*添加家居订单*/
        List<FurnitureOrder> furnitureOrderList = furnitureOrderService.findByStoreAndStatus(storeId, MallAndFurnitureOrderStatusEnum.DELIVERY.getCode());
        result.addAll(furnitureOrderList.stream().filter(furnitureOrder -> !(furnitureOrder.isInbound())).collect(Collectors.toList()));

        /*添加商城订单*/
        result.addAll(mallOrderService.findByStoreAndStatus(storeId, MallAndFurnitureOrderStatusEnum.DELIVERY.getCode()));

        /*排序*/
        result.sort(new OrderComparator());

        return result;
    }

    @Override
    public UserStatistical userCountStatistical(Long startTime, Long endTime, String country, String province, String city) {

        UserStatistical userStatistical = new UserStatistical();

        userStatistical.setConsumeCount(userService.countByConsumeStatusAndTimeAndArea(ConsumeStatusEnum.CONSUME.getCode(), startTime, endTime, country, province, city));
        userStatistical.setDepositCount(userService.countByDepositAndTimeAndArea(startTime, endTime, country, province, city));
        userStatistical.setUnConsumeCount(userService.countByConsumeStatusAndTimeAndArea(ConsumeStatusEnum.NOT_CONSUME.getCode(), startTime, endTime, country, province, city));
        userStatistical.setMemberCount(userService.countByBindingCardAndTimeAndArea(startTime, endTime, country, province, city));
        userStatistical.setAllCount(userStatistical.getConsumeCount() + userStatistical.getUnConsumeCount());

        return userStatistical;
    }

    @Override
    public Map<String, Integer> newOrderCount(Long startTime, Long endTime, String province, String city, String area, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*结果集*/
        Map<String, Integer> map = new HashMap<>(8);

        /*新订单件数*/
        Integer count;

          /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            //普通洗衣新订单
            count = laundryOrderService.countByTypeAndStatusAndAgentIdAndTimeAndArea(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.NEW.getCode(), role.getAccountId(), startTime, endTime, province, city, area);
            map.put("洗衣", count);

            //高端洗护
            count = laundryOrderService.countByTypeAndStatusAndAgentIdAndTimeAndArea(OrderTypeEnum.HIGH_LAUNDRY.getCode(), OrderStatusEnum.NEW.getCode(), role.getAccountId(), startTime, endTime, province, city, area);
            map.put("高端洗护", count);

            //家居
            count = furnitureOrderService.countByStatusAndAgentIdAndTimeAndArea(OrderStatusEnum.NEW.getCode(), role.getAccountId(), startTime, endTime, province, city, area);
            map.put("家具", count);

            //商城
            count = mallOrderService.countByStatusAndAgentIdAndTimeAndArea(OrderStatusEnum.NEW.getCode(), role.getAccountId(), startTime, endTime, province, city, area);
            map.put("商城", count);


        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //普通洗衣新订单
            count = laundryOrderService.countByTypeAndStatusAndTimeAndArea(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.NEW.getCode(), startTime, endTime, province, city, area);
            map.put("洗衣", count);

            //高端洗护
            count = laundryOrderService.countByTypeAndStatusAndTimeAndArea(OrderTypeEnum.HIGH_LAUNDRY.getCode(), OrderStatusEnum.NEW.getCode(), startTime, endTime, province, city, area);
            map.put("高端洗护", count);

            //家居
            count = furnitureOrderService.countByStatusAndTimeAndArea(OrderStatusEnum.NEW.getCode(), startTime, endTime, province, city, area);
            map.put("家具", count);

            //商城
            count = mallOrderService.countByStatusAndTimeAndArea(OrderStatusEnum.NEW.getCode(), startTime, endTime, province, city, area);
            map.put("商城", count);
        }


        return map;
    }

    @Override
    public List<OrderStatistical> orderAnalysis(Long startTime, Long endTime, String province, String city, String area, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        List<OrderStatistical> statisticalList = new ArrayList<>();

        OrderStatistical statistical = new OrderStatistical();

        /*平均单价*/
        Integer unitPrice = 0;

        /*数量*/
        Integer number = 0;

        /*微信支付*/
        Integer weChatPay = 0;

        /*余额支付*/
        Integer balancePay = 0;

        /*卡支付*/
        Integer cardPay = 0;

        List<LaundryOrder> laundryOrders;
        List<LaundryOrder> highLaundryOrders;
        List<FurnitureOrder> furnitureOrders;
        List<MallOrder> mallOrders;



        /*区别代理商  和 总部 看到的数据*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {

            laundryOrders = laundryOrderService.findByTypeAndStatusNotAndAgentIdAndTimeAndArea(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.CANCEL.getCode(), role.getAccountId(), startTime, endTime, province, city, area);

            highLaundryOrders = laundryOrderService.findByTypeAndStatusNotAndAgentIdAndTimeAndArea(OrderTypeEnum.HIGH_LAUNDRY.getCode(), OrderStatusEnum.CANCEL.getCode(), role.getAccountId(), startTime, endTime, province, city, area);

            furnitureOrders = furnitureOrderService.findByStatusNotAndAgentIdAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), role.getAccountId(), startTime, endTime, province, city, area);

            mallOrders = mallOrderService.findByStatusNotAndAgentIdAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), role.getAccountId(), startTime, endTime, province, city, area);

        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {

            laundryOrders = laundryOrderService.findByTypeAndStatusNotAndTimeAndArea(OrderTypeEnum.LAUNDRY.getCode(), OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

            highLaundryOrders = laundryOrderService.findByTypeAndStatusNotAndTimeAndArea(OrderTypeEnum.HIGH_LAUNDRY.getCode(), OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

            furnitureOrders = furnitureOrderService.findByStatusNotAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);

            mallOrders = mallOrderService.findByStatusNotAndTimeAndArea(OrderStatusEnum.CANCEL.getCode(), startTime, endTime, province, city, area);


        } else {
            throw new AccessDeniedException("权限不足");
        }


        /*普通洗衣订单分析*/
        statistical.setCategory("洗衣");
        statistical.setOrderCount(laundryOrders.size());
        for (LaundryOrder laundryOrder : laundryOrders) {

            if (laundryOrder.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                weChatPay++;
            } else if (laundryOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                cardPay++;
            } else {
                balancePay++;
            }
            unitPrice += AmountUtil.fenToYuan(laundryOrder.getAmount());
            number += laundryOrder.getItems().size();
        }


        //防止除零错误
        if (number == 0) {
            statistical.setUnitPrice(0);
        } else {
            statistical.setUnitPrice(unitPrice / number);
        }
        statistical.setNumber(number);
        statistical.setWeChatPay(weChatPay);
        statistical.setBalancePay(balancePay);
        statistical.setCardPay(cardPay);
        statisticalList.add(statistical);

         /*高端洗衣订单分析*/
        statistical = new OrderStatistical();
        unitPrice = 0;
        number = 0;
        weChatPay = 0;
        balancePay = 0;
        cardPay = 0;

        statistical.setCategory("高端洗护");
        statistical.setOrderCount(highLaundryOrders.size());

        for (LaundryOrder laundryOrder : highLaundryOrders) {

            if (laundryOrder.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                weChatPay++;
            } else if (laundryOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                cardPay++;
            } else {
                balancePay++;
            }
            unitPrice += AmountUtil.fenToYuan(laundryOrder.getAmount());
            number += laundryOrder.getItems().size();
        }

        if (number == 0) {
            statistical.setUnitPrice(0);
        } else {
            statistical.setUnitPrice(unitPrice / number);
        }
        statistical.setNumber(number);
        statistical.setWeChatPay(weChatPay);
        statistical.setCardPay(cardPay);
        statistical.setBalancePay(balancePay);
        statisticalList.add(statistical);


         /*家居订单分析*/
        statistical = new OrderStatistical();
        unitPrice = 0;
        number = 0;
        weChatPay = 0;
        balancePay = 0;
        cardPay = 0;

        statistical.setCategory("小让家居");
        statistical.setOrderCount(furnitureOrders.size());

        for (FurnitureOrder furnitureOrder : furnitureOrders) {

            if (furnitureOrder.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                weChatPay++;
            } else if (furnitureOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                cardPay++;
            } else {
                balancePay++;
            }
            unitPrice += AmountUtil.fenToYuan(furnitureOrder.getAmount());
            number += furnitureOrder.getItems().size();
        }
        if (number == 0) {
            statistical.setUnitPrice(0);
        } else {
            statistical.setUnitPrice(unitPrice / number);
        }
        statistical.setNumber(number);
        statistical.setWeChatPay(weChatPay);
        statistical.setBalancePay(balancePay);
        statistical.setCardPay(cardPay);
        statisticalList.add(statistical);

         /*商城订单分析*/
        statistical = new OrderStatistical();
        unitPrice = 0;
        number = 0;
        weChatPay = 0;
        balancePay = 0;
        cardPay = 0;

        statistical.setCategory("小让商城");
        statistical.setOrderCount(mallOrders.size());

        for (MallOrder mallOrder : mallOrders) {

            if (mallOrder.getPayMode().equals(PayModeEnum.WE_CHAT.getCode())) {
                weChatPay++;
            } else if (mallOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                cardPay++;
            } else {
                balancePay++;
            }
            unitPrice += AmountUtil.fenToYuan(mallOrder.getAmount());
            number += mallOrder.getItems().size();
        }
        if (number == 0) {
            statistical.setUnitPrice(0);
        } else {
            statistical.setUnitPrice(unitPrice / number);
        }
        statistical.setNumber(number);
        statistical.setWeChatPay(weChatPay);
        statistical.setBalancePay(balancePay);
        statistical.setCardPay(cardPay);
        statisticalList.add(statistical);

        return statisticalList;
    }

    private Map<String, Long> incomePercentage(Long laundryTotal, Long highLaundryTotal, Long furnitureTotal, Long mallTotal) {
        /*获取商户 和  平台收入*/

        Map<String, Long> result = new HashMap<>(3);

        //获取百分比
        List<Commission> commissions = commissionService.getAll();
        Long agentIncome = 0L;
        Long platformIncome = 0L;
        Long expressIncome = 0L;

        //平台收入 and 代理商收入计算
        for (Commission commission : commissions) {

            //单位是 %
            Integer agent = commission.getAgent();
            Integer platform = commission.getPlatform();
            Integer express = commission.getExpress();

            switch (commission.getKey()) {
                case "commissionLaundry":
                    agentIncome += agent * laundryTotal;
                    platformIncome += platform * laundryTotal;
                    expressIncome += express * laundryTotal;
                    break;
                case "commissionHighLaundry":
                    agentIncome += agent * highLaundryTotal;
                    platformIncome += platform * highLaundryTotal;
                    expressIncome += express * highLaundryTotal;
                    break;
                case "commissionFurniture":
                    agentIncome += agent * furnitureTotal;
                    platformIncome += platform * furnitureTotal;
                    expressIncome += express * furnitureTotal;
                    break;
                case "commissionMall":
                    agentIncome += agent * mallTotal;
                    platformIncome += platform * mallTotal;
                    expressIncome += express * mallTotal;
                    break;
                default:
            }
        }
        //结果除以100  因为是百分比
        result.put("agentIncome", agentIncome / 100);
        result.put("platformIncome", platformIncome / 100);
        result.put("expressIncome", expressIncome / 100);

        return result;
    }
}
          