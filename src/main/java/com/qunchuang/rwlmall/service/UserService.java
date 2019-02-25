package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.ConsumeRecord;
import com.qunchuang.rwlmall.domain.LaundryOrder;
import com.qunchuang.rwlmall.domain.Order;
import com.qunchuang.rwlmall.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/12 10:31
 */
public interface UserService {

    /*查找所有用户*/
    List<User> findAll();

    /*查找所有用户  分页*/
    Page<User> findAll(Pageable pageable);

    /*通过id  查找用户*/
    User findOne(String userId);

    /*通过openid 查找用户*/
    User findByOpenid(String openid);

    /*保存*/
    User save(User user);

    /*修改*/
    User update(String userId, User user);

    //统计用户总数
    Integer count();

    //按照用户消费信息、地区、时间分别统计用户人数 如10-100
    Integer countByConsumeStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String country, String province, String city);

    //已储值用户人数统计
    Integer countByDepositAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city);

    //已绑卡用户人数统计
    Integer countByBindingCardAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city);

    /*添加用户消费记录*/
    void addUserRecord(String userId, UserRecord userRecord);

    /*查询用户历史记录*/
    List<UserRecord> obtainUserRecord(String userId);

    /*查找用户的订单  时间降序*/
    List<Order> userOrders(String userId);

    /*按照用户消费信息、地区、时间分别查找用户*/
    Page<User> findByConsumeStatusAndTimeAndArea(Integer status, Long startTime, Long endTime, String country, String province, String city, Pageable pageable);

    /*按手机查询*/
    User findByPhone(String phone);

    /*查找用户完结订单*/
    List<Order> finishOrder(String userId);

    /*查找用户进行中的订单*/
    List<Order> carryOnOrder(String userId);

    /*按地区、时间分别查找用户人数*/
    Page<User> findByTimeAndArea(Long suserOrderstartTime, Long endTime, String country, String province, String city, Pageable pageable);

    /*依据用户openid判断用户是否存在*/
    boolean existsByOpenid(String openid);

    /*用户修改绑定的手机号*/
    void changePhone(String userId, String phoneNumber);

    /*绑定会员卡*/
    void bindingCard(String phone, String cno,String code, String userId);

    /*查询绑卡用户信息*/
    Page<User> findByBindingCardAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city, Pageable pageable);

    /*保存消费记录*/
    void addUserRecord(ConsumeRecord consumeRecord);

    /*注册手机号查询*/
    User findByRegisterPhone(String registerPhone);

    /*查询储蓄会员信息*/
    Page<User> findByDepositAndTimeAndArea(Long startTime, Long endTime, String country, String province, String city, Pageable pageable);

    /**
     * 恢复数据后  增加一条余额等价的充值记录
     */
    void initConsumeRecord();
}
