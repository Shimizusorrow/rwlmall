package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.Recharge;
import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.ConsumeRecord;
import com.qunchuang.rwlmall.domain.RechargeOrder;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.ConsumeStatusEnum;
import com.qunchuang.rwlmall.enums.PayStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.UserRecordEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.RechargeOrderRepository;
import com.qunchuang.rwlmall.service.RechargeOrderService;
import com.qunchuang.rwlmall.service.RechargeService;
import com.qunchuang.rwlmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/16 10:06
 */

@Service
public class RechargeOrderServiceImpl implements RechargeOrderService {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private RechargeOrderRepository recharOrderRepository;

    @Autowired
    private UserService userService;

    @Override
    public RechargeOrder create(String rechargeKey, String userId) {
        Recharge recharge = rechargeService.getRecharge(rechargeKey);
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setPayMoney(recharge.getPayMoney());
        rechargeOrder.setRewardMoney(recharge.getRewardMoney());
        rechargeOrder.setUserId(userId);
        return recharOrderRepository.save(rechargeOrder);
    }

    @Override
    public List<RechargeOrder> findByTimeAndPayStatus(Long startTime, Long endTime, Integer payStatus) {
        return recharOrderRepository.findByCreatetimeBetweenAndPayStatus(startTime,endTime,payStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized RechargeOrder paid(String orderId,Integer payMode) {
        //判断订单状态
        RechargeOrder rechargeOrder = findOne(orderId);

        //修改支付状态
        rechargeOrder.setPayStatus(PayStatusEnum.SUCCESS.getCode());

        //用户消费记录次数
        User user = userService.findOne(rechargeOrder.getUserId());
        user.setDeposit(true);
        user.setFrequency(user.getFrequency() + 1);

        //余额增加
        user.setBalance(user.getBalance() + rechargeOrder.getPayMoney() + rechargeOrder.getRewardMoney());

        userService.save(user);

        //用户消费记录保存
        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(rechargeOrder.getAmount());
        consumeRecord.setCategory(UserRecordEnum.RECHARGE.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setOrderId(orderId);
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        return recharOrderRepository.save(rechargeOrder);
    }

    @Override
    public RechargeOrder findOne(String orderId) {
        Optional<RechargeOrder> optional = recharOrderRepository.findById(Optional.ofNullable(orderId).orElse(""));
        if (!(optional.isPresent())) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_NOT_EXIST);
        }
        return optional.get();
    }
}
