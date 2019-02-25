package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.ConsumeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/7/16 14:30
 */
public interface ConsumeRecordService {

    /*分页查询用户的消费记录*/
    Page<ConsumeRecord> findByUserIdAndTime(String userId, Long startTime, Long endTime, Pageable pageable);

    /*分页查询用户的消费记录*/
    List<ConsumeRecord> findByUserIdAndTime(String userId, Long startTime, Long endTime, Sort sort);

    /*保存记录*/
    void save(ConsumeRecord consumeRecord);

    void saveAll(List<ConsumeRecord> consumeRecords);

}
