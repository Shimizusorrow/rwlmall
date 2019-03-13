package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.FreightSet;
import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.*;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.MallOrderRepository;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.DateUtil;
import com.qunchuang.rwlmall.utils.JiGuangMessagePushUtil;
import com.qunchuang.rwlmall.utils.WeChatUtil;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/3/13 13:59
 */

@SuppressWarnings("ALL")
@Service
public class MallOrderServiceImpl extends OrderServiceImpl<MallOrder> implements MallOrderService {

    @Autowired
    private MallOrderRepository mallOrderRepository;

    @Autowired
    private MallProductService mallProductService;

    @Autowired
    private UserService userService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private FreightSetService freightSetService;

    @Override
    public MallOrder cancelDispatch(String orderId) {
        MallOrder mallOrder = findOne(orderId);

        //已派订单撤回上一步
        if (MallAndFurnitureOrderStatusEnum.DELIVERY.getCode().equals(mallOrder.getStatus())) {
            //撤回到上一步
            mallOrder.setStoreId("");
            mallOrder.setStatus(OrderStatusEnum.NEW.getCode());
            String statusUpdateTime = mallOrder.getStatusUpdateTime();
            mallOrder.setStatusUpdateTime(statusUpdateTime.substring(0, statusUpdateTime.lastIndexOf(",")));


            return mallOrderRepository.save(mallOrder);
        }

        throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
    }

    @Override
    public List<MallOrder> findByCode(String code) {
        List<MallOrder> result = new ArrayList<>();
        //通过number查询
        if (code.length() == 12) {
            result.add(findByNumber(code));
        }

        //通过手机号查询
        if (code.length() == 11) {
            result.addAll(mallOrderRepository.findByPhone(code));
        }
        return result;
    }

    @Override
    public MallOrder save(MallOrder mallOrder) {
        return mallOrderRepository.save(mallOrder);
    }

    @Override
    public List<MallOrder> findByStoreAndStatusAndTime(String storeId, Integer status, Long startTime, Long endTime) {
        return mallOrderRepository.findByStoreIdAndStatusAndCreatetimeBetween(storeId, status, startTime, endTime);
    }

    @Override
    public List<MallOrder> findByStoreAndStatus(String storeId, Integer status) {
        return mallOrderRepository.findByStoreIdAndStatus(storeId, status);
    }

    @Override
    public void delivery(String orderId, String storeId, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        for (String id : orderId.split(",")) {
            MallOrder mallOrder = findOne(id);

             /*如果不是新订单则不能派送*/
            if (!(mallOrder.getStatus().equals(OrderStatusEnum.NEW.getCode()))) {
                throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
            }

             /*如果是代理商*/
            if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {

                if (role.getAccountId().equals(mallOrder.getAgentId())) {
                    mallOrder.setStoreId(storeId);
                    mallOrder.setStatus(MallAndFurnitureOrderStatusEnum.DELIVERY.getCode());
                    mallOrder.setStatusUpdateTime(mallOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
                    Store store = storeService.findOne(storeId);
                    mallOrder.setReceiptPeople(store.getName());
                    mallOrderRepository.save(mallOrder);

                    JiGuangMessagePushUtil.sendMessage(store.getNumber(), JiGuangMessagePushUtil.CONTENT);
                    WeChatUtil.sendMessage(redisService.getToken(), mallOrder.getOpenid(), "小让商城", mallOrder.getNumber(), mallOrder.getAmount(), "订单已分派请耐心等待", mallOrder.getCreatetime());

                } else {
                    throw new AccessDeniedException("权限不足");
                }

            } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
                //总部
                //在此再与代理商绑定   设置订单中代理商id
                Store store = storeService.findOne(storeId);
                mallOrder.setStoreId(storeId);
                mallOrder.setAgentId(store.getAgentId());
                mallOrder.setReceiptPeople(store.getName());
                mallOrder.setStatus(MallAndFurnitureOrderStatusEnum.DELIVERY.getCode());
                mallOrder.setStatusUpdateTime(mallOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
                mallOrderRepository.save(mallOrder);
                JiGuangMessagePushUtil.sendMessage(store.getNumber(), JiGuangMessagePushUtil.CONTENT);
                WeChatUtil.sendMessage(redisService.getToken(), mallOrder.getOpenid(), "小让商城", mallOrder.getNumber(), mallOrder.getAmount(), "订单已分派请耐心等待", mallOrder.getCreatetime());
            } else {
                throw new AccessDeniedException("权限不足");
            }
        }
    }

    @Override
    public void photoUnload(String orderId, String waybillImage, Principal principal) {

        MallOrder mallOrder = findOne(orderId);

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        //不是所派的门店  不能操作已派的订单
        if (!(role.getRoleNumber().equals(RoleEnum.STORE_ROLE.getCode())) && !(role.getAccountId().equals(mallOrder.getStoreId()))) {
            throw new AccessDeniedException("权限不足");
        }

        /*订单不是已派订单 不能发货*/
        if (!(OrderStatusEnum.DELIVERY.getCode().equals(mallOrder.getStatus()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
        mallOrder.setWaybillImage(waybillImage);
        mallOrder.setStatus(MallAndFurnitureOrderStatusEnum.SEND.getCode());
        mallOrder.setStatusUpdateTime(mallOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());
        mallOrderRepository.save(mallOrder);

    }

    @Override
    public List<MallOrder> findByStoreIdAndFinishTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status) {
        return mallOrderRepository.findByStoreIdAndFinishTimeBetweenAndStatus(storeId, startTime, endTime, status);
    }

    @Override
    public List<MallOrder> findByStoreIdAndTimeAndStatus(String storeId, Long startTime, Long endTime, Integer status) {
        return mallOrderRepository.findByStoreIdAndCreatetimeBetweenAndStatus(storeId, startTime, endTime, status);
    }

    @Override
    public Page<MallOrder> findByStatus(Integer status, Pageable pageable, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            return findByStatusAndAgentId(status, role.getAccountId(), pageable);
        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //总部
            return mallOrderRepository.findByStatus(status, pageable);
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public Page<MallOrder> findByStatusAndAgentId(Integer status, String agentId, Pageable pageable) {
        return mallOrderRepository.findByStatusAndAgentId(status, agentId, pageable);
    }

    @Override
    public List<MallOrder> findByStatus(Integer status) {
        return mallOrderRepository.findByStatus(status);
    }

    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallOrder create(MallOrder mallOrder, String userId) {
        /*判断库存是否充足*/
        Boolean judgeStock = mallProductService.judgeStock(mallOrder);
        if (judgeStock) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
        }

        /*总价*/
        Long orderAmount = 0L;

        /*购物车*/
        Set<MallOrderItem> mallOrderItems = mallOrder.getItems();

        //查询商品（数量  价格）
        for (MallOrderItem mallOrderItem : mallOrderItems) {
            MallProduct mallProduct = mallProductService.findOne(mallOrderItem.getMallProduct().getId());

            //判断商品是否下架
            if (ProductStatusEnum.DOWN.getCode().equals(mallProduct.getStatus())) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_NOT_UP);
            }

            //初步 计算商品总价
            Integer count = mallOrderItem.getCount();
            if (count <= 0) {
                throw new RwlMallException(ResultExceptionEnum.SHOPPING_CART_COUNT_NOT_TRUE);
            }
            orderAmount = orderAmount + (mallProduct.getPrice() * count);

            //直接接收的OrderItem  是不存在parentId的  所以需要手动添加
            mallOrderItem.setMallProduct(mallProduct);
            mallOrderItem.setParent(mallOrder);

        }

        //todo：商城 代理商匹配   不是按区域分配   改为在派给门店的时候 再去设置代理商
//        /*匹配订单到代理商*/
//        List<Agent> agentList = agentService.findAll();
//
//        /*设置订单中代理商id*/
//        for (Agent agent : agentList) {
//            if (agent.getRegionDistribution().contains(mallOrder.getArea())) {
//                mallOrder.setAgentId(agent.getId());
//                break;
//            }
//
//        }


        //获取设置的运费机制
        FreightSet freightSet = freightSetService.getFreightSet(FreightCategoryEnum.MALL.getKey());
        //如果价格不到包邮区间则 加上运费
        if (orderAmount < freightSet.getThreshold()) {
            orderAmount = orderAmount + freightSet.getFreight();
        }

        //设置金额 将订单 写入 数据库
        mallOrder.setAmount(orderAmount);
        //因为接口依旧存在  没有对未支付的新订单做过滤  但后期才发现 所以只能再加一个状态
        mallOrder.setStatusUpdateTime(DateUtil.currentTime());
        mallOrder.setStatus(OrderStatusEnum.WAIT_PAY.getCode());
        mallOrder.setPayStatus(PayStatusEnum.WAIT.getCode());
        mallOrder.setUserId(userId);
        User user = userService.findOne(userId);
        mallOrder.setOpenid(user.getOpenid());
        mallOrder.setRegisterPhone(user.getRegisterPhone());
        mallOrder.setBindPhone(user.getPhone());
        mallOrder = mallOrderRepository.save(mallOrder);

//        mallProductService.decreaseStock(mallOrder);

        return mallOrder;
    }


    @Override
    public Page<MallOrder> findByTimeAndArea(Long startTime, Long endTime, String province, String city, String area, Pageable pageable) {
        return mallOrderRepository.findByCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(startTime, endTime, province, city, area, pageable);
    }

    @Override
    public Integer countByStatusAndAgentIdAndTimeAndArea(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area) {
        return mallOrderRepository.countByStatusAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(status, agentId, startTime, endTime, province, city, area);
    }

    @Override
    public Integer countByStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area) {
        return mallOrderRepository.countByStatusAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContaining(status, startTime, endTime, province, city, area);
    }

    @Override
    public List<MallOrder> findByTimeAndStatus(Long startTime, Long endTime, Integer status) {
        return mallOrderRepository.findByCreatetimeBetweenAndStatus(startTime, endTime, status);
    }

    @Override
    public List<MallOrder> findByStatusNotAndAgentIdAndTimeAndArea(Integer status, String agentId, Long startTime, Long endTime, String province, String city, String area) {
        return mallOrderRepository.findByStatusNotAndAgentIdAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(status, agentId, startTime, endTime, province, city, area, PayStatusEnum.SUCCESS.getCode());
    }

    @Override
    public List<MallOrder> findByStatusNotAndTimeAndArea(Integer status, Long startTime, Long endTime, String province, String city, String area) {
        return mallOrderRepository.findByStatusNotAndCreatetimeBetweenAndProvinceContainingAndCityContainingAndAreaContainingAndPayStatus(status, startTime, endTime, province, city, area, PayStatusEnum.SUCCESS.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized MallOrder paid(String orderId, Integer payMode) {
        MallOrder mallOrder = findOne(orderId);

        try {
            //扣库存
            mallProductService.decreaseStock(mallOrder);
        } catch (RwlMallException e) {
            //todo: 2018年6月20日16:25:03 因为是微信回调才减库存， 所以可能存在（几率极低） 一个用户支付完成库存未减  另一用户发起支付 并成功后 库存已经不够
            e.printStackTrace();
        }

        //修改支付状态
        mallOrder.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        mallOrder.setPayMode(payMode);

        //修改订单状态 因为避免未支付订单会加入到系统 所以增加待支付订单  新订单即表示已支付订单
        mallOrder.setStatus(OrderStatusEnum.NEW.getCode());

        //用户消费记录次数  以及用户消费记录保存
        User user = userService.findOne(mallOrder.getUserId());
        if (!user.getConsumeStatus().equals(ConsumeStatusEnum.CONSUME.getCode())) {
            user.setConsumeStatus(ConsumeStatusEnum.CONSUME.getCode());
        }
        user.setFrequency(user.getFrequency() + 1);

        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(mallOrder.getAmount());
        consumeRecord.setCategory(UserRecordEnum.CONSUME.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setOrderId(orderId);
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        userService.save(user);

        return mallOrderRepository.save(mallOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized MallOrder cancel(String orderId) {
        MallOrder mallOrder = findOne(orderId);
        /*如果订单不是新订单  那么不能取消*/
        if (mallOrder.getStatus().equals(OrderStatusEnum.NEW.getCode()) || mallOrder.getStatus().equals(OrderStatusEnum.WAIT_PAY.getCode())) {
            mallOrder.setStatus(OrderStatusEnum.CANCEL.getCode());

            //退款
            if (mallOrder.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {

                Long amount;

                if (mallOrder.getPayMode().equals(PayModeEnum.CARD.getCode())) {
                    //取消了折扣  所以不用在取折扣价进行退款
//                    amount = laundryOrder.getCardAmount();
                    amount = mallOrder.getAmount();
                } else {
                    amount = mallOrder.getAmount();
                }

                User user = userService.findOne(mallOrder.getUserId());
                user.setBalance(user.getBalance() + amount);

                //返还库存
                mallProductService.increaseStock(mallOrder);

                //增加用户退款记录
                ConsumeRecord consumeRecord = new ConsumeRecord();
                consumeRecord.setMoney(amount);
                consumeRecord.setCategory(UserRecordEnum.REFUND.getMessage());
                consumeRecord.setBalance(user.getBalance());
                consumeRecord.setOrderId(orderId);
                consumeRecord.setUserId(user.getId());

                userService.addUserRecord(consumeRecord);

                userService.save(user);

                mallOrder.setPayStatus(PayStatusEnum.REFUND.getCode());
                WeChatUtil.sendMessage(redisService.getToken(), mallOrder.getOpenid(), "小让商城", mallOrder.getNumber(), mallOrder.getAmount(), "已取消，订单金额已转入余额", mallOrder.getCreatetime());

            }

            return mallOrderRepository.save(mallOrder);
        } else {
            throw new RwlMallException(ResultExceptionEnum.ORDER_STATUS_ERROR);
        }
    }


    @Override
    public MallOrder finish(String orderId) {
        MallOrder mallOrder = findOne(orderId);

        mallOrder.setStatus(OrderStatusEnum.FINISHED.getCode());
        mallOrder.setFinishTime(System.currentTimeMillis());
        mallOrder.setStatusUpdateTime(mallOrder.getStatusUpdateTime() + "," + DateUtil.currentTime());

        return mallOrderRepository.save(mallOrder);
    }


}
