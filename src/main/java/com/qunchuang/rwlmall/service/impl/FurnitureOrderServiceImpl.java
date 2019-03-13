package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.*;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.FurnitureOrderRepository;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import com.qunchuang.rwlmall.utils.DateUtil;
import com.qunchuang.rwlmall.utils.JiGuangMessagePushUtil;
import com.qunchuang.rwlmall.utils.WeChatUtil;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.repository.ConstructorRepository;

import java.security.Principal;
import java.util.*;

/**
 * @author Curtain
 * @date 2018/3/13 13:59
 */

@SuppressWarnings("ALL")
@Service
public class FurnitureOrderServiceImpl extends OrderServiceImpl<FurnitureOrder> implements FurnitureOrderService {

    @Autowired
    private FurnitureOrderRepository furnitureOrderRepository;

    @Autowired
    private FurnitureProductService furnitureProductService;

    @Autowired
    private UserService userService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RedisService redisService;


    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FurnitureOrder create(FurnitureOrder furnitureOrder, String userId) {
        /*判断库存是否充足*/
        if (furnitureProductService.judgeStock(furnitureOrder)) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
        }

        /*总价*/
        Long orderAmount = 0L;

        /*购物车*/
        Set<FurnitureOrderItem> furnitureOrderItems = furnitureOrder.getItems();

        //查询商品（数量  价格）
        for (FurnitureOrderItem furnitureOrderItem : furnitureOrderItems) {
            FurnitureProduct furnitureProduct = furnitureProductService.findOne(furnitureOrderItem.getFurnitureProduct().getId());

            //判断商品是否下架
            if (ProductStatusEnum.DOWN.getCode().equals(furnitureProduct.getStatus())) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_NOT_UP);
            }

            //初步 计算商品总价
            Integer count = furnitureOrderItem.getCount();
            if (count <= 0) {
                throw new RwlMallException(ResultExceptionEnum.SHOPPING_CART_COUNT_NOT_TRUE);
            }
            orderAmount = orderAmount + (furnitureProduct.getPrice() * count);

            //直接接收的OrderItem  是不存在parentId的  所以需要手动添加
            furnitureOrderItem.setFurnitureProduct(furnitureProduct);
            furnitureOrderItem.setParent(furnitureOrder);

        }
        //如果一种商品  下单后  数量是大于1的  则拷贝到resultOrderItem  直到count=1
        Set<FurnitureOrderItem> resultOrderItem = new HashSet<>();

        Iterator<FurnitureOrderItem> iterator = furnitureOrderItems.iterator();

        while (iterator.hasNext()) {
            FurnitureOrderItem furnitureOrderItem = iterator.next();

            int count = furnitureOrderItem.getCount();

            //拷贝OrderItem  count--
            while (count != 1) {
                FurnitureOrderItem orderItem = new FurnitureOrderItem();
                BeanUtils.copyProperties(furnitureOrderItem, orderItem);
                orderItem.setCount(1);
                resultOrderItem.add(orderItem);
                count--;
            }
            furnitureOrderItem.setCount(1);
            resultOrderItem.add(furnitureOrderItem);
        }

        //如果不是温州这不提供家居服务
        if (!("温州".equals(furnitureOrder.getCity()) || "温州市".equals(furnitureOrder.getCity()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_NOT_SERVICE);
        }

        /*匹配订单到代理商*/
        List<Agent> agentList = agentService.findAll();

        /*设置订单中代理商id*/
        for (Agent agent : agentList) {

            if (agent.getRegionDistribution().contains(furnitureOrder.getArea()) && agent.getRegion().contains(furnitureOrder.getCity())) {
                furnitureOrder.setAgentId(agent.getId());
                furnitureOrder.setServiceStore(agent.getAgentName());
                break;
            }

        }
        //如果没有匹配的代理商 则给莫好克代理商
        if (furnitureOrder.getAgentId() == null || "".equals(furnitureOrder.getAgentId())) {
            Agent agent = agentService.findByAgentName("莫好克温州代理商");
            if (agent != null) {
                furnitureOrder.setAgentId(agent.getId());
                furnitureOrder.setServiceStore(agent.getAgentName());
            }


        }


        //设置金额 将订单 写入 数据库
        furnitureOrder.setItems(resultOrderItem);
        furnitureOrder.setAmount(orderAmount);
        //因为接口依旧存在 但后期才发现 没有对未支付的新订单做过滤  所以只能再加一个状态
        furnitureOrder.setStatusUpdateTime(DateUtil.currentTime());
        furnitureOrder.setStatus(OrderStatusEnum.WAIT_PAY.getCode());
        furnitureOrder.setPayStatus(PayStatusEnum.WAIT.getCode());
        furnitureOrder.setUserId(userId);
        User user = userService.findOne(userId);
        furnitureOrder.setOpenid(user.getOpenid());
        furnitureOrder.setRegisterPhone(user.getRegisterPhone());
        furnitureOrder.setBindPhone(user.getPhone());
        furnitureOrder = furnitureOrderRepository.save(furnitureOrder);

        //扣库存
//        furnitureProductService.decreaseStock(furnitureOrder);

        return furnitureOrder;
    }

    @Override
    public void delivery(String orderId, String storeId, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        for (String id : orderId.split(",")) {

            FurnitureOrder furnitureOrder = findOne(id);

             /*如果不是新订单则不能派送*/
            if (!(furnitureOrder.getStatus().equals(OrderStatusEnum.NEW.getCode()))) {
                throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
            }

             /*如果是代理商*/
            if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {

                if (role.getAccountId().equals(furnitureOrder.getAgentId())) {
                    Store store = storeService.findOne(storeId);
                    furnitureOrder.setStoreId(storeId);
                    furnitureOrder.setReceiptPeople(store.getName());
                    furnitureOrder.setStatus(MallAndFurnitureOrderStatusEnum.DELIVERY.getCode());
                    furnitureOrder.setStatusUpdateTime(furnitureOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
                    furnitureOrderRepository.save(furnitureOrder);
                    JiGuangMessagePushUtil.sendMessage(store.getNumber(), JiGuangMessagePushUtil.CONTENT);
                    WeChatUtil.sendMessage(redisService.getToken(), furnitureOrder.getOpenid(), "小让家居", furnitureOrder.getNumber(), furnitureOrder.getAmount(), "宝贝送还中请当面查验", furnitureOrder.getCreatetime());

                } else {
                    throw new AccessDeniedException("权限不足");
                }

            } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
                //总部
                Store store = storeService.findOne(storeId);
                furnitureOrder.setStoreId(storeId);
                furnitureOrder.setReceiptPeople(store.getName());
                furnitureOrder.setStatus(MallAndFurnitureOrderStatusEnum.DELIVERY.getCode());


                Agent agent = agentService.findOne(store.getAgentId());
                furnitureOrder.setServiceStore(agent.getAgentName());
                furnitureOrder.setAgentId(agent.getId());
                furnitureOrder.setStatusUpdateTime(furnitureOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());

                furnitureOrderRepository.save(furnitureOrder);
                JiGuangMessagePushUtil.sendMessage(store.getNumber(), JiGuangMessagePushUtil.CONTENT);
                WeChatUtil.sendMessage(redisService.getToken(), furnitureOrder.getOpenid(), "小让家居", furnitureOrder.getNumber(), furnitureOrder.getAmount(), "订单已分派请耐心等待", furnitureOrder.getCreatetime());
            } else {
                throw new AccessDeniedException("权限不足");
            }

        }
    }

    @Override
    public List<FurnitureOrder> findByCode(String code) {
        List<FurnitureOrder> result = new ArrayList<>();
        //通过number查询
        if (code.length() == 12) {
            result.add(findByNumber(code));
        }

        //通过手机号查询
        if (code.length() == 11) {
            result.addAll(furnitureOrderRepository.findByPhone(code));
        }
        return result;
    }

    @Override
    public FurnitureOrder save(FurnitureOrder furnitureOrder) {
        return furnitureOrderRepository.save(furnitureOrder);
    }

    @Override
    public FurnitureOrder cancelDispatch(String orderId) {
        FurnitureOrder furnitureOrder = findOne(orderId);

        //已派订单撤回上一步
        if (OrderStatusEnum.DELIVERY.getCode().equals(furnitureOrder.getPayStatus())) {

            //撤回到上一步
            furnitureOrder.setStatus(OrderStatusEnum.NEW.getCode());
            String statusUpdateTime = furnitureOrder.getStatusUpdateTime();
            furnitureOrder.setStatusUpdateTime(statusUpdateTime.substring(0, statusUpdateTime.lastIndexOf(",")));

            return furnitureOrderRepository.save(furnitureOrder);
        }


        throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
    }

    @Override
    public List<FurnitureOrder> findByStoreAndStatusAndTime(String storeId, Integer status, Long startTime, Long endTime) {
        return furnitureOrderRepository.findByStoreIdAndStatusAndCreatetimeBetween(storeId, status, startTime, endTime);
    }

    @Override
    public void photoUnload(String orderId, List<FurnitureOrderItem> furnitureOrderItemList, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        FurnitureOrder furnitureOrder = findOne(orderId);

        if (!role.getAccountId().equals(furnitureOrder.getStoreId())) {
            throw new AccessDeniedException("权限不足");
        }

        for (FurnitureOrderItem furnitureOrderItem : furnitureOrderItemList) {
            for (FurnitureOrderItem item : furnitureOrder.getItems()) {

                if (item.getId().equals(furnitureOrderItem.getId())) {
                    item.setImage(furnitureOrderItem.getImage());
                    item.setStatus(LaundryCommodityEnum.INBOUND.getCode());
                }
            }
        }
        FurnitureOrder order = furnitureOrderRepository.save(furnitureOrder);

        //判断是否所有的商品都已入站  是 则整单入站
        int count = 0;
        for (FurnitureOrderItem orderItem : order.getItems()) {
            if ((LaundryCommodityEnum.INBOUND.getCode()).equals(orderItem.getStatus())) {
                count++;
            }
        }

        //标记订单所有条目 已经拍照上传
        if (order.getItems().size() == count) {
            order.setInbound(true);
            order.setStatus(MallAndFurnitureOrderStatusEnum.SEND.getCode());
            furnitureOrder.setStatusUpdateTime(furnitureOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
            furnitureOrderRepository.save(order);
        }

    }

    @Override
    public List<FurnitureOrder> findByStoreIdAndFinishTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status) {
        return furnitureOrderRepository.findByStoreIdAndFinishTimeBetweenAndStatus(storeId, startTime, endTime, status);
    }

    @Override
    public List<FurnitureOrder> findByStoreIdAndTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status) {
        return furnitureOrderRepository.findByStoreIdAndCreatetimeBetweenAndStatus(storeId, startTime, endTime, status);
    }

    @Override
    public Integer countByStatusAndAgentIdAndTimeAndArea(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area) {
        return furnitureOrderRepository.countByStatusAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(status, agentId, startTime, endTime, province, city, area);
    }

    @Override
    public Integer countByStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area) {
        return furnitureOrderRepository.countByStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(status, startTime, endTime, province, city, area);
    }

    @Override
    public List<FurnitureOrder> findByStatusNotAndAgentIdAndTimeAndArea(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area) {
        return furnitureOrderRepository.findByStatusNotAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(status, agentId, startTime, endTime, province, city, area, PayStatusEnum.SUCCESS.getCode());
    }

    @Override
    public List<FurnitureOrder> findByStatusNotAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area) {
        return furnitureOrderRepository.findByStatusNotAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(status, startTime, endTime, province, city, area, PayStatusEnum.SUCCESS.getCode());
    }

    @Override
    public Page<FurnitureOrder> findByTimeAndArea(Long startTime, Long endTime, String province, String city, String area, Pageable pageable) {
        return furnitureOrderRepository.findByCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(startTime, endTime, province, city, area, pageable);
    }

    @Override
    public List<FurnitureOrder> findByStoreAndStatus(String storeId, Integer status) {
        return furnitureOrderRepository.findByStoreIdAndStatus(storeId, status);
    }

    @Override
    public Page<FurnitureOrder> findByStatus(Integer status, Pageable pageable, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            return findByStatusAndAgentId(status, role.getAccountId(), pageable);
        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //总部
            return furnitureOrderRepository.findByStatus(status, pageable);
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public Page<FurnitureOrder> findByStatusAndAgentId(Integer status, String agentId, Pageable pageable) {
        return furnitureOrderRepository.findByStatusAndAgentId(status, agentId, pageable);
    }

    @Override
    public List<FurnitureOrder> findByStatus(Integer status) {
        return furnitureOrderRepository.findByStatus(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized FurnitureOrder paid(String orderId, Integer payMode) {
        FurnitureOrder furnitureOrder = findOne(orderId);

        try {
            //扣库存
            furnitureProductService.decreaseStock(furnitureOrder);
        } catch (RwlMallException e) {
            e.printStackTrace();
        }

        //修改支付状态
        furnitureOrder.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        furnitureOrder.setPayMode(payMode);

        //修改订单状态 因为避免未支付订单会加入到系统 所以增加待支付订单  新订单即表示已支付订单
        furnitureOrder.setStatus(OrderStatusEnum.NEW.getCode());


        //用户消费记录次数  以及用户消费记录保存
        User user = userService.findOne(furnitureOrder.getUserId());
        if (!user.getConsumeStatus().equals(ConsumeStatusEnum.CONSUME.getCode())) {
            user.setConsumeStatus(ConsumeStatusEnum.CONSUME.getCode());
        }
        user.setFrequency(user.getFrequency() + 1);

        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(furnitureOrder.getAmount());
        consumeRecord.setCategory(UserRecordEnum.CONSUME.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setOrderId(orderId);
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        userService.save(user);

        return furnitureOrderRepository.save(furnitureOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized FurnitureOrder cancel(String orderId) {
        FurnitureOrder furnitureOrder = findOne(orderId);
        /*如果订单不是新订单  那么不能作废*/

        if (furnitureOrder.getStatus().equals(OrderStatusEnum.NEW.getCode()) || furnitureOrder.getStatus().equals(OrderStatusEnum.WAIT_PAY.getCode())) {
            furnitureOrder.setStatus(OrderStatusEnum.CANCEL.getCode());

            //退款
            if (furnitureOrder.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
                Long amount;

                if (furnitureOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                    //取消了折扣  所以不用在取折扣价进行退款
//                    amount = laundryOrder.getCardAmount();
                    amount = furnitureOrder.getAmount();
                } else {
                    amount = furnitureOrder.getAmount();
                }

                User user = userService.findOne(furnitureOrder.getUserId());
                user.setBalance(user.getBalance() + amount);

                //返还库存
                furnitureProductService.increaseStock(furnitureOrder);

                //增加用户退款记录
                ConsumeRecord consumeRecord = new ConsumeRecord();
                consumeRecord.setMoney(amount);
                consumeRecord.setCategory(UserRecordEnum.REFUND.getMessage());
                consumeRecord.setBalance(user.getBalance());
                consumeRecord.setOrderId(orderId);
                consumeRecord.setUserId(user.getId());

                userService.addUserRecord(consumeRecord);

                userService.save(user);

                furnitureOrder.setPayStatus(PayStatusEnum.REFUND.getCode());
                WeChatUtil.sendMessage(redisService.getToken(), furnitureOrder.getOpenid(), "小让家居", furnitureOrder.getNumber(), furnitureOrder.getAmount(), "已取消，订单金额已转入余额", furnitureOrder.getCreatetime());

            }

            return furnitureOrderRepository.save(furnitureOrder);
        } else {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
    }

    @Override
    public FurnitureOrder finish(String orderId) {
        FurnitureOrder furnitureOrder = findOne(orderId);

          /*订单状态不是已发或已派 不能完结*/
        if (furnitureOrder.getStatus().equals(MallAndFurnitureOrderStatusEnum.SEND.getCode()) || furnitureOrder.getStatus().equals(MallAndFurnitureOrderStatusEnum.DELIVERY.getCode())) {
            furnitureOrder.setStatus(OrderStatusEnum.FINISHED.getCode());
            furnitureOrder.setStatusUpdateTime(furnitureOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
            furnitureOrder.setFinishTime(System.currentTimeMillis());

            return furnitureOrderRepository.save(furnitureOrder);
        }
        throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
    }

}


