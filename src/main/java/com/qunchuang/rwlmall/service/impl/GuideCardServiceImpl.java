package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.ConsumeRecord;
import com.qunchuang.rwlmall.domain.GuideCard;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.UserRecordEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.GuideCardRepository;
import com.qunchuang.rwlmall.service.GuideCardService;
import com.qunchuang.rwlmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/24 14:35
 */

@Service
public class GuideCardServiceImpl implements GuideCardService {

    @Autowired
    private GuideCardRepository guideCardRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GuideCard save(GuideCard guideCard, Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        User user = userService.findByPhone(guideCard.getUserPhone());
        guideCard.setUserCreateTime(user.getCreatetime());
        guideCard.setUserNumber(user.getNumber());

        guideCard.setOldBalance(user.getBalance());
        user.setBalance(user.getBalance()+guideCard.getMoney());
        guideCard.setNewBalance(user.getBalance());
        guideCard.setTransferPeople(role.getUsername());

        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setMoney(guideCard.getMoney());
        consumeRecord.setCategory(UserRecordEnum.REFUND.getMessage());
        consumeRecord.setBalance(user.getBalance());
        consumeRecord.setUserId(user.getId());

        userService.addUserRecord(consumeRecord);

        userService.save(user);

        return guideCardRepository.save(guideCard);
    }

    @Override
    public Page<GuideCard> findAll(Pageable pageable) {
        return guideCardRepository.findAll(pageable);
    }

    @Override
    public GuideCard findOne(String guideCardId) {
        Optional<GuideCard> optional = guideCardRepository.findById(Optional.ofNullable(guideCardId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.GUIDE_CARD_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public Page<GuideCard> findByUserPhone(String userPhone, Pageable pageable) {
        return guideCardRepository.findByUserPhone(userPhone,pageable);
    }
}
