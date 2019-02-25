package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.OldOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/9/3 10:23
 */
public interface OldOrderService {

    /**
     * 通过number查询
     * @param number
     * @return
     */
    Optional<OldOrder> findByNumber(String number);

    /**
     * 通过userId查询  分页
     * @param userId
     * @param pageable
     * @return
     */
    Page<OldOrder> findByUserId(String userId, Pageable pageable);

    /**
     * 通过userId查询 所有
     * @param userId
     * @return
     */
    List<OldOrder> findByUserId(String userId);

    /**
     * 查询所有 分页
     * @param pageable
     * @return
     */
    Page<OldOrder> findAll(Pageable pageable);

    /**
     *初始化刚刚导入的数据（id统一。）
     */
//    void initData();

}
