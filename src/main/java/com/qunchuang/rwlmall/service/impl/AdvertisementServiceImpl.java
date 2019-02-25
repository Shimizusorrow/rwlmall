package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Advertisement;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.AdvertisementRepository;
import com.qunchuang.rwlmall.service.AdvertisementService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/14 13:47
 */

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public Advertisement findByType(Integer type) {
        Optional<Advertisement> optional = advertisementRepository.findByType(type);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public Advertisement save(Advertisement advertisement) {
        if(!Optional.ofNullable(advertisement).isPresent()){
            throw new RwlMallException(ResultExceptionEnum.ADVERTISEMENT_NOT_EXIST);
        }
        Integer type = advertisement.getType();
        Advertisement result = findByType(type);
        if (result!=null){
            delete(result.getId());
        }
        return advertisementRepository.save(advertisement);
    }

    @Override
    public Advertisement update(String advertisementId, Advertisement advertisement) {
        Advertisement result = findOne(advertisementId);
        BeanUtils.copyProperties(advertisement,result, BeanCopyUtil.getNullPropertyNames(advertisement));
        return advertisementRepository.save(result);
    }

    @Override
    public void delete(String advertisementId) {
        Advertisement advertisement = findOne(advertisementId);
        advertisementRepository.delete(advertisement);
    }

    @Override
    public Advertisement findOne(String advertisementId) {
        Optional<Advertisement> optional = advertisementRepository.findById(Optional.ofNullable(advertisementId).orElse(""));

        if(!optional.isPresent()){
            throw new RwlMallException(ResultExceptionEnum.ADVERTISEMENT_NOT_EXIST);
        }
        return optional.get();
    }


}
