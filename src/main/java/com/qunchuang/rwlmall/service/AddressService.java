package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Address;
import com.qunchuang.rwlmall.domain.Agent;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/12 10:32
 */
public interface AddressService {

    /*根据地址id 查询一条地址*/
    Address findOne(String addressId);

    /*按添加时间倒序获取用户的所有地址*/
    List<Address> findByUserId(String userId);

    /*查找用户默认地址*/
    Address userDefaultAddress(String userId);

    /*修改地址*/
    Address update(String addressId,Address address);

    /*修改默认地址*/
    void changeStatus(String userId,String addressId);

    /*增加地址*/
    void save(String userId, Address address);

    /*删除地址*/
    void deleteAddress(String userId, String addressId);


}
