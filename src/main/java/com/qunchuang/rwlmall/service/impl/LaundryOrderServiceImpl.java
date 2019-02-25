package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.FreightSet;
import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.*;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.LaundryItemRepository;
import com.qunchuang.rwlmall.repository.LaundryOrderRepository;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import com.qunchuang.rwlmall.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

/**
 * @author Curtain
 * @date 2018/3/13 13:59
 */

@SuppressWarnings("ALL")
@Service
public class LaundryOrderServiceImpl extends OrderServiceImpl<LaundryOrder> implements LaundryOrderService {

    @Autowired
    private LaundryOrderRepository laundryOrderRepository;

    @Autowired
    private LaundryProductService laundryProductService;

    @Autowired
    private UserService userService;

    @Autowired
    private SFService sfService;

    @Autowired
    private FreightSetService freightSetService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private LaundryItemRepository laundryItemRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RedisService redisService;

    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaundryOrder create(LaundryOrder laundryOrder, String userId) {
        /*判断库存是否充足*/
        Boolean judgeStock = laundryProductService.judgeStock(laundryOrder);
        if (judgeStock) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
        }

        /*总价*/
        Long orderAmount = 0L;

        /*购物车*/
        Set<LaundryOrderItem> laundryOrderItems = laundryOrder.getItems();


        //查询商品（数量  价格）
        for (LaundryOrderItem laundryOrderItem : laundryOrderItems) {
            LaundryProduct laundryProduct = laundryProductService.findOne(laundryOrderItem.getLaundryProduct().getId());

            //判断商品是否下架
            if (ProductStatusEnum.DOWN.getCode().equals(laundryProduct.getStatus())) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_NOT_UP);
            }

            //计算商品总价
            Integer count = laundryOrderItem.getCount();
            if (count <= 0) {
                throw new RwlMallException(ResultExceptionEnum.SHOPPING_CART_COUNT_NOT_TRUE);
            }
            orderAmount = orderAmount + (laundryProduct.getPrice() * count);

            //直接接收的OrderItem  是不存在parentId的  所以需要手动添加
            laundryOrderItem.setLaundryProduct(laundryProduct);
            laundryOrderItem.setParent(laundryOrder);
        }

        //如果一种商品  下单后  数学是大于1的  则拷贝到resultOrderItem  直到count=1
        Set<LaundryOrderItem> resultOrderItem = new HashSet<>();

        Iterator<LaundryOrderItem> iterator = laundryOrderItems.iterator();

        while (iterator.hasNext()) {
            LaundryOrderItem laundryOrderItem = iterator.next();

            int count = laundryOrderItem.getCount();

            //拷贝OrderItem  count--
            while (count != 1) {
                LaundryOrderItem orderItem = new LaundryOrderItem();
                BeanUtils.copyProperties(laundryOrderItem, orderItem);
                orderItem.setCount(1);
                resultOrderItem.add(orderItem);
                count--;
            }
            laundryOrderItem.setCount(1);
            resultOrderItem.add(laundryOrderItem);
        }

        //获取设置的运费机制
        FreightSet freightSet = freightSetService.getFreightSet();
        //如果价格不到包邮区间则 加上运费
        if (orderAmount < freightSet.getThreshold()) {
            orderAmount = orderAmount + freightSet.getFreight();
        }

        /*匹配订单到代理商*/
        List<Agent> agentList = agentService.findAll();

        /*设置订单中代理商id*/
        for (Agent agent : agentList) {
            if (agent.getRegionDistribution().contains(laundryOrder.getArea()) && agent.getRegion().contains(laundryOrder.getCity())) {
                laundryOrder.setAgentId(agent.getId());
                laundryOrder.setServiceStore(agent.getAgentName());
                break;
            }

        }

        //如果没有匹配的代理商 则将订单归属到莫好克温州代理商
        if (laundryOrder.getAgentId() == null || "".equals(laundryOrder.getAgentId())) {
            Agent agent = agentService.findByAgentName("莫好克温州代理商");
            if (agent != null) {
                laundryOrder.setAgentId(agent.getId());
                laundryOrder.setServiceStore(agent.getAgentName());
            }
        }


        //设置金额 将订单 写入 数据库
        laundryOrder.setStatusUpdateTime(DateUtil.currentTime());
        laundryOrder.setItems(resultOrderItem);
        laundryOrder.setAmount(orderAmount);
        //因为接口依旧存在 但后期才发现 没有对未支付的新订单做过滤  所以只能再加一个状态
        laundryOrder.setStatus(OrderStatusEnum.WAIT_PAY.getCode());
        laundryOrder.setPayStatus(PayStatusEnum.WAIT.getCode());
        laundryOrder.setUserId(userId);

        User user = userService.findOne(userId);
        laundryOrder.setOpenid(user.getOpenid());
        laundryOrder.setRegisterPhone(user.getRegisterPhone());
        laundryOrder.setBindPhone(user.getPhone());

        laundryOrder = laundryOrderRepository.save(laundryOrder);

//        laundryProductService.decreaseStock(laundryOrder);

        return laundryOrder;
    }

    @Override
    @Transactional
    public void storePickup(String orderId, String storeId, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();


        String[] orderIds = orderId.split(",");

        /*订单中添加门店信息*/
        for (String id : orderIds) {

            LaundryOrder order = findOne(id);

             /*如果是代理商  or 代理商管理员*/
            if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {

                if (role.getAccountId().equals(order.getAgentId())) {
                    Store store = storeService.findOne(storeId);
                    order.setCollectMode(SFStatusEnum.STORE.getCode());
                    order.setStoreId(storeId);
                    order.setReceiptPeople(store.getName());
                    delivery(order);
                } else {
                    throw new AccessDeniedException("权限不足");
                }

            } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
                //总部 or 总部管理员
                order.setCollectMode(SFStatusEnum.STORE.getCode());
                Store store = storeService.findOne(storeId);
                order.setReceiptPeople(store.getName());
                order.setStoreId(storeId);
                Agent agent = agentService.findOne(store.getAgentId());
                order.setServiceStore(agent.getAgentName());
                order.setAgentId(agent.getId());
                delivery(order);
            } else {
                throw new AccessDeniedException("权限不足");
            }

        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sfCollect(String orderId, String storeId) {
        sfService.dispatchOrder(orderId, storeId, SFStatusEnum.COLLECT.getCode());
        LaundryOrder order = findOne(orderId);
        order.setStoreId(storeId);
        order.setReceiptPeople("顺丰物流人员");
        order.setCollectMode(SFStatusEnum.SF.getCode());

        Store store = storeService.findOne(storeId);
        Agent agent = agentService.findOne(store.getAgentId());
        order.setServiceStore(agent.getAgentName());
        order.setAgentId(agent.getId());


        delivery(order);

    }

    @Override
    public void storeGiveBack(String orderId, String storeId, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();


        String[] orderIds = orderId.split(",");

        /*订单中添加门店信息*/
        for (String id : orderIds) {

            LaundryOrder order = findOne(id);

             /*如果是代理商  or 代理商管理员*/
            if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {

                if (role.getAccountId().equals(order.getAgentId())) {
                    order.setGiveBackStoreId(storeId);
                    order.setCollectMode(SFStatusEnum.STORE.getCode());
                    giveBack(order);
                } else {
                    throw new AccessDeniedException("权限不足");
                }

            } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
                //总部 or 总部管理员
                order.setGiveBackStoreId(storeId);
                order.setCollectMode(SFStatusEnum.STORE.getCode());
                giveBack(order);
            } else {
                throw new AccessDeniedException("权限不足");
            }

        }

    }

    @Override
    public void sfSend(String orderId, String storeId) {
        sfService.dispatchOrder(orderId, storeId, SFStatusEnum.SEND.getCode());
        LaundryOrder order = findOne(orderId);
        order.setGiveBackStoreId(storeId);
        order.setCollectMode(SFStatusEnum.SF.getCode());
        giveBack(order);

    }

    @Override
    public void giveBack(LaundryOrder laundryOrder) {
         /*如果不是上挂订单则不能派送*/
        if (!(laundryOrder.getStatus().equals(OrderStatusEnum.HANG_ON.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
        /*订单超时状态 改为不超时*/
        laundryOrder.setTimeOut(LaundryOrderTimeEnum.NORMAL.getCode());

        laundryOrder.setStatus(OrderStatusEnum.GIVE_BACK.getCode());
        laundryOrder.setStatusUpdateTime(laundryOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
        laundryOrderRepository.save(laundryOrder);



    }

    @Override
    public void delivery(LaundryOrder laundryOrder) {
        /*如果不是新订单则不能派送*/
        if (!(laundryOrder.getStatus().equals(OrderStatusEnum.NEW.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
        laundryOrder.setStatus(OrderStatusEnum.DELIVERY.getCode());
        laundryOrder.setStatusUpdateTime(laundryOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
        laundryOrderRepository.save(laundryOrder);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized LaundryOrder paid(String orderId, Integer payMode) {

        LaundryOrder laundryOrder = findOne(orderId);
/*
        //判断订单状态
        if (!(laundryOrder.getStatus() == OrderStatusEnum.NEW.getCode())) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR, "paid() orderId=" + laundryOrder.getId() + "orderStatus=" + laundryOrder.getStatus());
        }

        //判断支付状态
        if (!laundryOrder.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_PAY_STATUS_ERROR, "paid()  payStatus = " + laundryOrder.getPayStatus());
        }
*/
        //扣库存
        try {
            laundryProductService.decreaseStock(laundryOrder);
        } catch (RwlMallException e) {
            e.printStackTrace();
            //todo: 2018年6月20日16:25:03 因为是微信回调才减库存， 所以可能存在（几率极低） 一个用户支付完成库存未减  另一用户发起支付 并成功后 库存已经不够
        }

        //修改支付状态
        laundryOrder.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        laundryOrder.setPayMode(payMode);

        //修改订单状态 因为避免未支付订单会加入到系统 所以增加待支付订单  新订单即表示已支付订单
        laundryOrder.setStatus(OrderStatusEnum.NEW.getCode());


        //用户消费记录次数  以及用户消费记录保存
        User user = userService.findOne(laundryOrder.getUserId());
        if (!user.getConsumeStatus().equals(ConsumeStatusEnum.CONSUME.getCode())) {
            user.setConsumeStatus(ConsumeStatusEnum.CONSUME.getCode());
        }

        user.setFrequency(user.getFrequency() + 1);

        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(laundryOrder.getAmount());
        consumeRecord.setCategory(UserRecordEnum.CONSUME.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setOrderId(orderId);
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        userService.save(user);


        return laundryOrderRepository.save(laundryOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized LaundryOrder cancel(String orderId) {
        LaundryOrder laundryOrder = findOne(orderId);
        /*如果订单不是新订单  那么不能作废*/
        if (laundryOrder.getStatus().equals(OrderStatusEnum.NEW.getCode()) || laundryOrder.getStatus().equals(OrderStatusEnum.WAIT_PAY.getCode())) {
            laundryOrder.setStatus(OrderStatusEnum.CANCEL.getCode());

            //退款
            if (laundryOrder.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
                Long amount;

                if (laundryOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                    //取消了折扣  所以不用在取折扣价进行退款
//                    amount = laundryOrder.getCardAmount();
                    amount = laundryOrder.getAmount();
                } else {
                    amount = laundryOrder.getAmount();
                }
                User user = userService.findOne(laundryOrder.getUserId());
                user.setBalance(user.getBalance() + amount);

                //返还库存
                laundryProductService.increaseStock(laundryOrder);

                //增加用户退款记录
                ConsumeRecord consumeRecord = new ConsumeRecord();
                consumeRecord.setMoney(amount);
                consumeRecord.setCategory(UserRecordEnum.REFUND.getMessage());
                consumeRecord.setBalance(user.getBalance());
                consumeRecord.setOrderId(orderId);
                consumeRecord.setUserId(user.getId());

                userService.addUserRecord(consumeRecord);

                userService.save(user);
                laundryOrder.setPayStatus(PayStatusEnum.REFUND.getCode());


            }

            return laundryOrderRepository.save(laundryOrder);
        } else {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
    }

    @Override
    public LaundryOrder finish(String orderId) {
        LaundryOrder laundryOrder = findOne(orderId);

        /*订单状态不是送还或派单 不能完结*/
        if (OrderStatusEnum.GIVE_BACK.getCode().equals(laundryOrder.getStatus()) || OrderStatusEnum.DELIVERY.getCode().equals(laundryOrder.getStatus())) {

            laundryOrder.setStatus(OrderStatusEnum.FINISHED.getCode());

            laundryOrder.setFinishTime(System.currentTimeMillis());
            laundryOrder.setStatusUpdateTime(laundryOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());

            return laundryOrderRepository.save(laundryOrder);
        }

        throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);

    }

    @Override
    public String distinctBarCode(String barCode) {
        //时间  说又不需要去重操作
//        List<LaundryOrderItem> orderItemList = laundryItemRepository.findByBarCode(barCode);
//
//        if (orderItemList.size() > 0) {
//            throw new RwlMallException(ResultExceptionEnum.ORDER_BARCODE_EXIST);
//        }
        return null;
    }

    @Override
    public List<LaundryOrder> findByTypeAndCode(Integer type, String code) {

        List<LaundryOrder> result = new ArrayList<>();
        List<LaundryOrder> laundryOrders = new ArrayList<>();

        //通过条形码查询订单
        if (code.length() == 7) {
            List<LaundryOrderItem> laundryOrderItemList = laundryItemRepository.findByBarCode(code);
            for (LaundryOrderItem laundryOrderItem : laundryOrderItemList) {
                laundryOrders.add(laundryOrderItem.getParent());
            }
        }

        //通过number查询
        if (code.length() == 12) {
            laundryOrders.add(findByNumber(code));
        }

        //通过手机号查询
        if (code.length() == 11) {
            laundryOrders = laundryOrderRepository.findByPhone(code);
        }

        //通过type
        for (LaundryOrder laundryOrder : laundryOrders) {
            if (laundryOrder.getType().equals(type)) {
                //向结果集添加符合的订单
                result.add(laundryOrder);
            }
        }

        return result;
    }

    @Override
    public LaundryOrder save(LaundryOrder laundryOrder) {
        return laundryOrderRepository.save(laundryOrder);
    }

    @Override
    public LaundryOrder cancelDispatch(String orderId) {
        LaundryOrder laundryOrder = findOne(orderId);

        //已派订单撤回上一步
        if (OrderStatusEnum.DELIVERY.getCode().equals(laundryOrder.getStatus())) {
            //是顺丰收单 不能撤回
            if (SFStatusEnum.SF.getCode().equals(laundryOrder.getCollectMode())) {
                throw new RwlMallException(ResultExceptionEnum.ORDER_CANCEL_FAIL);
            }

            //撤回到上一步
            laundryOrder.setStoreId("");
            laundryOrder.setStatus(OrderStatusEnum.NEW.getCode());
            // 状态时间也要撤回  删掉最后一段时间
            String statusUpdateTime = laundryOrder.getStatusUpdateTime();
            laundryOrder.setStatusUpdateTime(statusUpdateTime.substring(0, statusUpdateTime.lastIndexOf(",")));

            return laundryOrderRepository.save(laundryOrder);

        }

        //送还订单订单撤回
        if (OrderStatusEnum.GIVE_BACK.getCode().equals(laundryOrder.getStatus())) {
            //是顺丰收单 不能撤回
            if (SFStatusEnum.SF.getCode().equals(laundryOrder.getCollectMode())) {
                throw new RwlMallException(ResultExceptionEnum.ORDER_CANCEL_FAIL);
            }
            //如果是超时订单  重新改为超时
            Long inboundTime = laundryOrder.getInboundTime();
            if ((inboundTime < (System.currentTimeMillis() - 7 * DateUtil.DAY_TIME_STAMP)) && (LaundryOrderTimeEnum.NORMAL.getCode().equals(laundryOrder.getTimeOut()))) {
                laundryOrder.setTimeOut(LaundryOrderTimeEnum.TIME_OUT.getCode());
            }

            //撤回到上一步
            laundryOrder.setGiveBackStoreId("");
            laundryOrder.setStatus(OrderStatusEnum.HANG_ON.getCode());
            //状态时间也要撤回  删掉最后一段时间
            String statusUpdateTime = laundryOrder.getStatusUpdateTime();
            laundryOrder.setStatusUpdateTime(statusUpdateTime.substring(0, statusUpdateTime.lastIndexOf(",")));


            return laundryOrderRepository.save(laundryOrder);

        }

        throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
    }

    @Override
    public List<LaundryOrder> findByItemBarCode(String barCode, Principal principal) {
        List<LaundryOrder> laundryOrders = new ArrayList<>();
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        LaundryOrder laundryOrder;
        List<LaundryOrderItem> items = laundryItemRepository.findByBarCode(barCode);

        for (LaundryOrderItem item : items) {
            laundryOrder = item.getParent();
            //订单属于此门店  订单状态等于上挂  并且不重复
            if (role.getAccountId().equals(laundryOrder.getStoreId()) && (!laundryOrders.contains(laundryOrder)) && (OrderStatusEnum.INBOUND.getCode().equals(laundryOrder.getStatus()))) {
                laundryOrders.add(laundryOrder);
            }
        }

        return laundryOrders;

    }

    @Override
    public List<LaundryOrder> findByTypeAndStoreAndStatusAndTime(Integer type, String storeId, Integer status, Long startTime, Long endTime) {
        return laundryOrderRepository.findByTypeAndStoreIdAndStatusAndCreatetimeBetween(type, storeId, status, startTime, endTime);
    }

    @Override
    public List<LaundryOrder> findByStoreIdAndTimeAndStatusAndType(String storeId, Long startTime, Long endTime, Integer status, Integer type) {
        return laundryOrderRepository.findByStoreIdAndCreatetimeBetweenAndStatusAndType(storeId, startTime, endTime, status, type);
    }

    @Override
    public List<LaundryOrder> findByStoreIdAndFinishTimeAndStatusAndType(String storeId, Long startTime, Long endTime, Integer status, Integer type) {
        return laundryOrderRepository.findByStoreIdAndFinishTimeBetweenAndStatusAndType(storeId, startTime, endTime, status, type);
    }

    //    @Override
//    public LaundryOrder received(String orderId) {
//        LaundryOrder laundryOrder = findOne(orderId);
//        /*如果不是派送订单则不能改为已收*/
//        if (!(laundryOrder.getStatus().equals(OrderStatusEnum.DELIVERY.getCode()))) {
//            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
//        }
//
//        laundryOrder.setStatus(OrderStatusEnum.RECEIVED.getCode());
//        return laundryOrderRepository.save(laundryOrder);
//    }

    @Override
    public LaundryOrder inboundRemark(String orderId, String remark, Principal principal) {
        LaundryOrder order = findOne(orderId);

        order.setInboundRemark(remark);

        return laundryOrderRepository.save(order);
    }

    @Override
    public LaundryOrder inbound(String orderId) {
        LaundryOrder laundryOrder = findOne(orderId);
        /*如果不是已派订单则不能入站*/
        if (!(laundryOrder.getStatus().equals(OrderStatusEnum.DELIVERY.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
        laundryOrder.setStatus(OrderStatusEnum.INBOUND.getCode());
        laundryOrder.setInboundTime(System.currentTimeMillis());
        laundryOrder.setStatusUpdateTime(laundryOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
        LaundryOrder order = laundryOrderRepository.save(laundryOrder);


        return order;
    }

    @Override
    public LaundryOrder hangOn(String orderId, Principal principal) {
        LaundryOrder laundryOrder = findOne(orderId);

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        //不是所派的门店  不能操作已派的订单
        if (!(role.getRoleNumber().equals(RoleEnum.STORE_ROLE.getCode())) && !(role.getAccountId().equals(laundryOrder.getStoreId()))) {
            throw new AccessDeniedException("权限不足");
        }

        /*如果不是入站订单则不能上挂*/
        if (!(laundryOrder.getStatus().equals(OrderStatusEnum.INBOUND.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
        laundryOrder.setStatus(OrderStatusEnum.HANG_ON.getCode());
        laundryOrder.setStatusUpdateTime(laundryOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
        return laundryOrderRepository.save(laundryOrder);
    }

    @Override
    public LaundryOrder timeOut(String orderId) {
        LaundryOrder laundryOrder = findOne(orderId);

        /*将状态修改为 超时*/
        laundryOrder.setTimeOut(LaundryOrderTimeEnum.TIME_OUT.getCode());

        return laundryOrderRepository.save(laundryOrder);
    }


    @Override
    public List<LaundryOrder> findByTimeOutAndType(Integer timeOut, Integer type) {
        return laundryOrderRepository.findByTimeOutAndType(timeOut, type);
    }

    @Override
    public Page<LaundryOrder> findByType(Integer type, Pageable pageable) {
        return laundryOrderRepository.findByType(type, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void singletonInbound(String orderId, List<LaundryOrderItem> laundryOrderItemList, Principal principal) {
        //获取整个订单  并将传入的商品入站
        LaundryOrder order = findOne(orderId);

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        //不是所派的门店  不能操作已派的订单
        if (!(role.getRoleNumber().equals(RoleEnum.STORE_ROLE.getCode())) && !(role.getAccountId().equals(order.getStoreId()))) {
            throw new AccessDeniedException("权限不足");
        }

        //遍历购物详情
        for (LaundryOrderItem laundryOrderItem : laundryOrderItemList) {

            for (LaundryOrderItem orderItem : order.getItems()) {
                //找到匹配的商品  然后入站
                if (orderItem.getId().equals(laundryOrderItem.getId())) {
                    BeanUtils.copyProperties(laundryOrderItem, orderItem, BeanCopyUtil.getNullPropertyNames(laundryOrderItem));
                    orderItem.setStatus(LaundryCommodityEnum.INBOUND.getCode());

           /*         String barCode = laundryOrderItem.getBarCode();
                    List<LaundryOrderItem> orderItemList = laundryItemRepository.findByBarCode(barCode);

                    //因为这里似乎  jpa自动在beanUtils 拷贝的时候 已经持久化了
                    // 导致 laundryItemRepository.findByBarCode 查出来一定是大于等于1的
                    //所以 size >0  改为 size>1判断
                    if (orderItemList.size() > 1) {
                        throw new RwlMallException(ResultExceptionEnum.ORDER_BARCODE_EXIST);
                    }*/

                    break;
                }
            }
        }

        order = laundryOrderRepository.save(order);
        //判断是否所有的商品都已入站  是 则整单入站
        int count = 0;
        for (LaundryOrderItem orderItem : order.getItems()) {
            if ((LaundryCommodityEnum.INBOUND.getCode()).equals(orderItem.getStatus())) {
                count++;
            }
        }

        //将订单状态改为入站
        if (order.getItems().size() == count) {
            inbound(orderId);
        }
    }

    @Override
    public void saveAll(List<LaundryOrder> list) {
        laundryOrderRepository.saveAll(list);
    }

    @Override
    public List<LaundryOrder> findByTypeAndStatus(Integer type, Integer status) {
        return laundryOrderRepository.findByTypeAndStatus(type, status);
    }

    @Override
    public List<LaundryOrder> findByStoreAndStatusAndType(String storeId, Integer status, Integer type) {
        return laundryOrderRepository.findByStoreIdAndStatusAndType(storeId, status, type);
    }


    @Override
    public Page<LaundryOrder> findByTypeAndTimeAndArea(Integer type, Long startTime, Long endTime, String province, String city, String area, Pageable pageable) {
        return laundryOrderRepository.findByTypeAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(type, startTime, endTime, province, city, area, pageable);
    }

    @Override
    public Page<LaundryOrder> findByTypeAndStatusAndArea(Integer type, Integer status, String province, String city, String area, Pageable pageable, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            Agent agent = agentService.findOne(role.getAccountId());

            if (agent.getRegionDistribution().contains(area) && agent.getRegion().contains(city)) {
                return laundryOrderRepository.findByTypeAndStatusAndProvinceContainingAndCityContainingAndAreaContaining(type, status, province, city, area, pageable);
            }
            throw new AccessDeniedException("权限不足");

        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //总部
            return laundryOrderRepository.findByTypeAndStatusAndProvinceContainingAndCityContainingAndAreaContaining(type, status, province, city, area, pageable);
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public void singletonHangOn(String orderId, String orderItemId, Principal principal) {


        //获取整个订单  将对应的商品上挂
        LaundryOrder order = findOne(orderId);


        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        //不是所派的门店  不能操作已派的订单
        if (!(role.getRoleNumber().equals(RoleEnum.STORE_ROLE.getCode())) && !(role.getAccountId().equals(order.getStoreId()))) {
            throw new AccessDeniedException("权限不足");
        }

        for (LaundryOrderItem orderItem : order.getItems()) {
            if (orderItem.getId().equals(orderItemId)) {
                orderItem.setStatus(LaundryCommodityEnum.HANG_ON.getCode());
                order = laundryOrderRepository.save(order);
                break;
            }
        }
        //判断是否所有的商品都已上挂  是 则整单上挂
        int count = 0;
        for (LaundryOrderItem orderItem : order.getItems()) {
            if ((LaundryCommodityEnum.HANG_ON.getCode()).equals(orderItem.getStatus())) {
                count++;
            }
        }

        //将订单状态改为上挂
        if (order.getItems().size() == count) {
            hangOn(orderId, principal);
        }

    }

    @Override
    public Page<LaundryOrder> findByTypeAndStatusAndAgentId(Integer type, Integer status, String agentId, Pageable pageable) {
        return laundryOrderRepository.findByTypeAndStatusAndAgentId(type, status, agentId, pageable);
    }

    @Override
    public Page<LaundryOrder> findByTypeAndStatus(Integer type, Integer status, Pageable pageable, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            return findByTypeAndStatusAndAgentId(type, status, role.getAccountId(), pageable);
        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //总部
            return laundryOrderRepository.findByTypeAndStatus(type, status, pageable);
        }
        throw new AccessDeniedException("权限不足");


    }

    @Override
    public Integer countByTypeAndStatusAndAgentIdAndTimeAndArea(Integer type, Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area) {
        return laundryOrderRepository.countByTypeAndStatusAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(type, status, agentId, startTime, endTime, province, city, area);
    }

    @Override
    public Integer countByTypeAndStatusAndTimeAndArea(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area) {
        return laundryOrderRepository.countByTypeAndStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(type, status, startTime, endTime, province, city, area);
    }

    @Override
    public List<LaundryOrder> findByTypeAndStatusNotAndAgentIdAndTimeAndArea(Integer type, Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area) {
        return laundryOrderRepository.findByTypeAndStatusNotAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(type, status, agentId, startTime, endTime, province, city, area, PayStatusEnum.SUCCESS.getCode());
    }

    @Override
    public List<LaundryOrder> findByTypeAndStatusNotAndTimeAndArea(Integer type, Integer status, Long startTime, Long endTime, String province, String city, String area) {
        return laundryOrderRepository.findByTypeAndStatusNotAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(type, status, startTime, endTime, province, city, area, PayStatusEnum.SUCCESS.getCode());
    }
}
