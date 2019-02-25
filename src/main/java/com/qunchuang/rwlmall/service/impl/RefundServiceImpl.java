package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.UserRecordEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.RefundRepository;
import com.qunchuang.rwlmall.service.RedisService;
import com.qunchuang.rwlmall.service.RefundService;
import com.qunchuang.rwlmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/25 14:29
 */

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    @Transactional
    public Refund save(Refund refund, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        User user = userService.findByPhone(refund.getUserPhone());
        refund.setUserCreateTime(user.getCreatetime());
        refund.setUserNumber(user.getNumber());

        refund.setOldBalance(user.getBalance());
        refund.setNewBalance(user.getBalance()+refund.getMoney());
        user.setBalance(user.getBalance()+refund.getMoney());
        refund.setTransferPeople(role.getUsername());

        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(refund.getMoney());
        consumeRecord.setCategory(UserRecordEnum.REFUND.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        userService.save(user);



        return refundRepository.save(refund);
    }

    @Override
    public Page<Refund> findAll(Pageable pageable) {
        return refundRepository.findAll(pageable);
    }

    @Override
    public Page<Refund> findByUserPhone(String userPhone, Pageable pageable) {
        return refundRepository.findByUserPhone(userPhone,pageable);
    }

    @Override
    public Refund findOne(String refundId) {
        Optional<Refund> optional = refundRepository.findById(Optional.ofNullable(refundId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.REFUND_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public List<Refund> findByTime(Long startTime, Long endTime) {
        return refundRepository.findByCreatetimeBetween(startTime,endTime);
    }
}
