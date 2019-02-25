package com.qunchuang.rwlmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qunchuang.rwlmall.bean.CardInfo;
import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.ConsumeRecord;
import com.qunchuang.rwlmall.domain.OldOrder;
import com.qunchuang.rwlmall.domain.Order;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.ConsumeStatusEnum;
import com.qunchuang.rwlmall.enums.OrderStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.UserRepository;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import com.qunchuang.rwlmall.utils.CardPayUtil;
import com.qunchuang.rwlmall.utils.OrderComparator;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Curtain
 * @date 2018/3/12 19:03
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Autowired
    private FurnitureOrderService furnitureOrderService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConsumeRecordService consumeRecordService;

    @Autowired
    private OldOrderService oldOrderService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findOne(String userId) {
        Optional<User> optional = userRepository.findById(Optional.ofNullable(userId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.USER_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public User findByOpenid(String openid) {
        Optional<User> optional = userRepository.findByOpenid(Optional.ofNullable(openid).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.USER_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public User save(User user) {
        if (!Optional.ofNullable(user).isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.USER_INFO_NOT_TRUE);
        }
        return userRepository.save(user);
    }

    @Override
    public User update(String userId, User user) {
        User result = findOne(userId);
        Long balance = result.getBalance();
        BeanUtils.copyProperties(user, result, BeanCopyUtil.getNullPropertyNames(user));
        result.setBalance(balance);
        return userRepository.save(result);
    }


    @Override
    public Integer count() {
        return userRepository.countByIdNotNull();
    }


    @Override
    public Integer countByBindingCardAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city) {
        return userRepository.countByBindingCardIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(startTime, endTime, country, province, city);
    }

    @Override
    public Integer countByDepositAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city) {
        return userRepository.countByDepositIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(startTime, endTime, country, province, city);
    }

    @Override
    public Integer countByConsumeStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String country, String province, String city) {
        return userRepository.countByConsumeStatusAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(status, startTime, endTime, country, province, city);
    }

    @Override
    public Page<User> findByConsumeStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String country, String province, String city, Pageable pageable) {
        return userRepository.findByConsumeStatusAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(status, startTime, endTime, country, province, city, pageable);
    }

    @Override
    public List<Order> userOrders(String userId) {
        List<Order> list = new ArrayList<>();
        list.addAll(laundryOrderService.findByUserId(userId));
        list.addAll(furnitureOrderService.findByUserId(userId));
        list.addAll(mallOrderService.findByUserId(userId));
        list.sort(new OrderComparator());
        return list;
    }

    @Override
    public void initConsumeRecord() {
        List<User> userList = userRepository.findAll();
        List<ConsumeRecord> consumeRecords = new ArrayList<>();
        for (User user : userList) {
            //生成充值记录
            if (user.getBalance() > 0) {
                ConsumeRecord consumeRecord = new ConsumeRecord();
                consumeRecord.setUserId(user.getId());
                consumeRecord.setBalance(0L);
                consumeRecord.setCategory("充值");
                consumeRecord.setMoney(user.getBalance());
            }
            consumeRecordService.saveAll(consumeRecords);
        }
    }

    @Override
    public List<Order> finishOrder(String userId) {
        List<Order> result;
        List<Order> orders = userOrders(userId);

        result = orders.stream().filter(order -> (order.getStatus().equals(OrderStatusEnum.FINISHED.getCode()) || order.getStatus().equals(OrderStatusEnum.CANCEL.getCode())))
                .collect(Collectors.toList());

        //增加旧数据订单
        List<OldOrder> orderList = oldOrderService.findByUserId(userId);

        for (OldOrder oldOrder : orderList) {
            //过滤掉未支付的订单
            if (oldOrder.getAmount() > 0) {
                result.add(((Order) oldOrder));
            }

        }

        result.sort(new OrderComparator());

        return result;
    }

    @Override
    public Page<User> findByDepositAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city, Pageable pageable) {
        return userRepository.findByDepositIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(startTime, endTime, country, province, city, pageable);
    }

    @Override
    public User findByRegisterPhone(String registerPhone) {
        return userRepository.findByRegisterPhone(registerPhone);
    }

    @Override
    public void addUserRecord(ConsumeRecord consumeRecord) {
        consumeRecordService.save(consumeRecord);
    }

    @Override
    public Page<User> findByBindingCardAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city, Pageable pageable) {
        return userRepository.findByBindingCardIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(startTime, endTime, country, province, province, pageable);
    }

    @Override
    public void bindingCard(String phone, String cno, String code, String userId) {

        User user = findOne(userId);
        List<CardInfo> cardInfoList = CardPayUtil.query(phone);

        //验证code
        String key = userId + phone;
        if (!(redisTemplate.hasKey(key))) {
            throw new RwlMallException(ResultExceptionEnum.CODE_NOT_TRUE);
        }
        String rs = (String) redisTemplate.opsForValue().get(key);

        if (!(rs.equals(code))) {
            throw new RwlMallException(ResultExceptionEnum.CODE_NOT_TRUE);
        }

//        判断卡手机号  和注册手机号 或 在用手机号 是否相同
        if (!(phone.equals(user.getRegisterPhone())) && (!(phone.equals(user.getPhone())))) {
            throw new RwlMallException(ResultExceptionEnum.XW_CARD_PHONE_WRONG);
        }

        //判断卡的类型
        for (CardInfo cardInfo : cardInfoList) {
            if (cardInfo.getCno().equals(cno)) {
                if (cardInfo.getType().contains("无折扣卡")) {
                    throw new RwlMallException(ResultExceptionEnum.XW_CARD_TYPE_WRONG);
                }

                if (!(cardInfo.getStatus().equals(1))) {
                    throw new RwlMallException(ResultExceptionEnum.XW_CARD_STATUS_WRONG);
                }

                if (cardInfo.getLocked().equals(2)) {
                    throw new RwlMallException(ResultExceptionEnum.XW_CARD_STATUS_WRONG);
                }

                if (user.getBindingCard() && (user.getCno().equals(cardInfo.getCno()))) {
                    throw new RwlMallException(ResultExceptionEnum.XW_CARD_REPEAT);
                }


                user.setBindingCard(true);

                user.setDiscount(cardInfo.getDiscount());
                user.setCid(cardInfo.getCid());
                user.setCno(cardInfo.getCno());
                userRepository.save(user);

                redisTemplate.delete(key);
                return;
            }
        }

        throw new RwlMallException(ResultExceptionEnum.XW_CARD_NOT_TRUE);
    }

    @Override
    public void changePhone(String userId, String phoneNumber) {
        String time = (String) redisTemplate.opsForValue().get(userId + "phone");

        //判断时间是否在30分钟内
        if (time == null || System.currentTimeMillis() - Long.valueOf(time) > 1800000) {
            throw new RwlMallException(ResultExceptionEnum.EXPIRED_VERIFICATION);
        } else {
            Optional<User> phone = userRepository.findByPhone(phoneNumber);
            User registerPhone = findByRegisterPhone(phoneNumber);

            //手机号防止重复
            if (!(phone.isPresent()) && !(userId.equals(phone.get().getId()))) {
                throw new RwlMallException(ResultExceptionEnum.PHONE_IS_REGISTER);
            }

            //注册手机号防止重复
            if (registerPhone != null && !(userId.equals(registerPhone.getId()))) {
                throw new RwlMallException(ResultExceptionEnum.PHONE_IS_REGISTER);
            }

            //修改手机号
            User user = findOne(userId);

            //判断手机号是否相同
            if (phoneNumber.equals(user.getPhone())) {
                throw new RwlMallException(ResultExceptionEnum.PHONE_IS_EXIST);
            }
            user.setPhone(phoneNumber);
            userRepository.save(user);
        }

    }


    @Override
    public boolean existsByOpenid(String openid) {
        return userRepository.existsByOpenid(openid);
    }

    @Override
    public Page<User> findByTimeAndArea(Long startTime, Long endTime, String country, String province, String city, Pageable pageable) {
        return userRepository.findByCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(startTime, endTime, country, province, city, pageable);
    }

    @Override
    public List<Order> carryOnOrder(String userId) {
        List<Order> result;
        List<Order> orders = userOrders(userId);


        result = orders.stream().filter(order -> (!(order.getStatus().equals(OrderStatusEnum.FINISHED.getCode()) || order.getStatus().equals(OrderStatusEnum.CANCEL.getCode()))))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public User findByPhone(String phone) {
        Optional<User> optional = userRepository.findByPhone(Optional.ofNullable(phone).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.USER_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public List<UserRecord> obtainUserRecord(String userId) {
        String key = "UserRecord" + userId;
        List<String> list = redisTemplate.opsForList().range(key, 0, 100);
        List<UserRecord> result = new ArrayList<>();
        for (String value : list) {
            JSONObject userRecord = (JSONObject) JSON.parse(value);
            result.add(JSONObject.toJavaObject(userRecord, UserRecord.class));
        }

        return result;
    }

    @Override
    public void addUserRecord(String userId, UserRecord userRecord) {
        String key = "UserRecord" + userId;
        String value = JSONObject.toJSONString(userRecord);
        redisTemplate.opsForList().leftPush(key, value);
    }


}
