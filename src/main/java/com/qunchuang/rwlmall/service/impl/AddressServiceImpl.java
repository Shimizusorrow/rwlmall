package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Address;
import com.qunchuang.rwlmall.enums.AddressEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.AddressRepository;
import com.qunchuang.rwlmall.service.AddressService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/13 10:28
 */

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address findOne(String addressId) {
        Optional<Address> optional = addressRepository.findById(Optional.ofNullable(addressId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ADDRESS_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public Address userDefaultAddress(String userId) {
        List<Address> addressList = findByUserId(userId);
        for (Address address : addressList) {
            if (AddressEnum.DEFAULT.getCode().equals(address.getStatus())) {
                return address;
            }
        }
        throw new RwlMallException(ResultExceptionEnum.ADDRESS_NOT_EXIST);
    }

    @Override
    public List<Address> findByUserId(String userId) {
        List<Address> list = addressRepository.findByUserId(Optional.ofNullable(userId).orElse(""), Sort.by("createtime"));
        //将默认地址 放在第一条
        Address defaultAddress = null;

        for (Address address : list) {
            if (address.getStatus().equals(AddressEnum.DEFAULT.getCode())) {
                defaultAddress = address;
                break;
            }
        }

        if (defaultAddress != null) {
            list.remove(defaultAddress);
            list.add(0, defaultAddress);
        }
        return list;
    }

    @Override
    public Address update(String addressId, Address address) {
        Address result = findOne(addressId);
        Integer addressStatus = result.getStatus();

        BeanUtils.copyProperties(address, result, BeanCopyUtil.getNullPropertyNames(address));

        //如果更新的是默认地址  则 更新后依然设置为默认地址
        result.setStatus(addressStatus);
        return addressRepository.save(result);
    }

    @Override
    @Transactional
    public void changeStatus(String userId, String addressId) {
        List<Address> addressList = findByUserId(userId);

        for (Address address : addressList) {
            address.setStatus(AddressEnum.UN_DEFAULT.getCode());
            /*将指定的地址设置为默认地址 */
            if (address.getId().equals(addressId)){
                address.setStatus(AddressEnum.DEFAULT.getCode());
            }
        }
        addressRepository.saveAll(addressList);
    }

    @Override
    public void save(String userId, Address address) {
        if (!Optional.ofNullable(address).isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ADDRESS_NOT_EXIST);
        }
        address.setUserId(userId);
        //如果这是用户的第一条地址  则将该条地址改为默认地址
        List<Address> list = findByUserId(userId);
        if (list.size() == 0) {
            address.setStatus(AddressEnum.DEFAULT.getCode());
        }
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(String userId, String addressId) {
        Integer rs = findOne(addressId).getStatus();
        if (rs.equals(AddressEnum.DEFAULT.getCode())) {
            for (Address address : findByUserId(userId)) {
                if (!(address.getId().equals(addressId))) {
                    address.setStatus(AddressEnum.DEFAULT.getCode());
                    break;
                }
            }
        }
        addressRepository.deleteById(addressId);
    }
}
