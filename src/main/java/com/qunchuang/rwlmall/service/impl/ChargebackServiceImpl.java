package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.UserRecordEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.ChargebackRepository;
import com.qunchuang.rwlmall.service.ChargebackService;
import com.qunchuang.rwlmall.service.RedisService;
import com.qunchuang.rwlmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/11/14 8:28
 */
@Service
public class ChargebackServiceImpl implements ChargebackService {

    @Autowired
    private ChargebackRepository chargebackRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public Chargeback save(Chargeback chargeback, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        User user = userService.findByPhone(chargeback.getUserPhone());
        chargeback.setUserCreateTime(user.getCreatetime());
        chargeback.setUserNumber(user.getNumber());

        chargeback.setOldBalance(user.getBalance());

        long newBalance = user.getBalance() - chargeback.getMoney();

        if (newBalance<0){
            throw new RwlMallException(ResultExceptionEnum.CHARGEBACK_FAIL);
        }

        chargeback.setNewBalance(newBalance);
        user.setBalance(newBalance);
        chargeback.setTransferPeople(role.getUsername());

        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(chargeback.getMoney());
        consumeRecord.setCategory(UserRecordEnum.CHARGE_BACK.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        userService.save(user);



        return chargebackRepository.save(chargeback);
    }

    @Override
    public Page<Chargeback> findAll(Pageable pageable) {
        return chargebackRepository.findAll(pageable);
    }

    @Override
    public Page<Chargeback> findByUserPhone(String userPhone, Pageable pageable) {
        return chargebackRepository.findByUserPhone(userPhone,pageable);
    }

    @Override
    public Chargeback findOne(String chargeId) {
        Optional<Chargeback> optional = chargebackRepository.findById(Optional.ofNullable(chargeId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.CHARGEBACK_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public List<Chargeback> findByTime(Long startTime, Long endTime) {
        return chargebackRepository.findByCreatetimeBetween(startTime,endTime);
    }
}
