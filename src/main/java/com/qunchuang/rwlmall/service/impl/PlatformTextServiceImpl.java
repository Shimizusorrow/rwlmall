package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.PlatformText;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.PlatformTextRepository;
import com.qunchuang.rwlmall.service.PlatformTextService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/28 14:50
 */

@Service
public class PlatformTextServiceImpl implements PlatformTextService {

    @Autowired
    private PlatformTextRepository platformTextRepository;

    @Override
    public PlatformText findByType(Integer type) {
        return platformTextRepository.findByType(type);
    }

    @Override
    public PlatformText save(PlatformText platformText) {

        if(!Optional.ofNullable(platformText).isPresent()){
            throw new RwlMallException(ResultExceptionEnum.PLATFORM_TEXT_NOT_EXIST);
        }
        Integer type = platformText.getType();
        PlatformText result = findByType(type);
        if (result!=null){
            platformTextRepository.delete(result);
        }

        return platformTextRepository.save(platformText);
    }

    @Override
    public PlatformText update(PlatformText platformText, String platformTextId) {

        Optional<PlatformText> optional = platformTextRepository.findById(Optional.ofNullable(platformTextId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.PRODUCT_NOT_EXIST);
        }
        PlatformText result = optional.get();
        BeanUtils.copyProperties(platformText, result, BeanCopyUtil.getNullPropertyNames(platformText));

        return platformTextRepository.save(result);
    }
}
