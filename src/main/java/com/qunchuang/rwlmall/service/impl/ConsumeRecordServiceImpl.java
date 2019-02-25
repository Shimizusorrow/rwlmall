package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.ConsumeRecord;
import com.qunchuang.rwlmall.repository.ConsumeRecordRepository;
import com.qunchuang.rwlmall.service.ConsumeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/7/16 14:30
 */

@Service
public class ConsumeRecordServiceImpl implements ConsumeRecordService {

    @Autowired
    private ConsumeRecordRepository consumeRecordRepository;

    @Override
    public void saveAll(List<ConsumeRecord> consumeRecords) {
        consumeRecordRepository.saveAll(consumeRecords);
    }

    @Override
    public void save(ConsumeRecord consumeRecord) {
        consumeRecordRepository.save(consumeRecord);
    }

    @Override
    public List<ConsumeRecord> findByUserIdAndTime(String userId, Long startTime, Long endTime, Sort sort) {
        return consumeRecordRepository.findByUserIdAndCreatetimeBetween(userId,startTime,endTime,sort);
    }

    @Override
    public Page<ConsumeRecord> findByUserIdAndTime(String userId, Long startTime, Long endTime, Pageable pageable) {
        return consumeRecordRepository.findByUserIdAndCreatetimeBetween(userId, startTime, endTime, pageable);
    }
}
