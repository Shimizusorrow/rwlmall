package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.OldOrder;
import com.qunchuang.rwlmall.domain.OldOrderItem;
import com.qunchuang.rwlmall.repository.OldOrderRepository;
import com.qunchuang.rwlmall.service.OldOrderService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/9/3 10:24
 */

@Service
public class OldOrderServiceImpl implements OldOrderService {

    @Autowired
    private OldOrderRepository oldOrderRepository;

    @Override
    public Optional<OldOrder> findByNumber(String number) {
        return oldOrderRepository.findByNumber(number);
    }

    @Override
    public Page<OldOrder> findByUserId(String userId, Pageable pageable) {
        return oldOrderRepository.findByUserId(userId,pageable);
    }

//    @Override
//    public void initData() {
//        List<OldOrder> oldOrderList = oldOrderRepository.findAll();
//
//        for (OldOrder oldOrder : oldOrderList){
//            String id = oldOrder.getId();
//            String userId = oldOrder.getUserId();
//            //修改id 和 userId
//            if (id.endsWith("A07")){
//                id = id.substring(0,id.lastIndexOf("A07"));
//                oldOrder.setId(id+"A27");
//            }
//
//            if (userId.endsWith("A12")){
//                userId = userId.substring(0,id.lastIndexOf("A12"));
//                oldOrder.setUserId(userId+"A02");
//            }
//
//            for (OldOrderItem oldOrderItem : oldOrder.getItems()){
//                oldOrderItem.getParent().setId(id+"A27");
//                String itemId = oldOrderItem.getId();
//                if (itemId.endsWith("A08")){
//                    itemId = itemId.substring(0,itemId.lastIndexOf("A08"));
//                    oldOrderItem.setId(itemId+"A28");
//                }
//            }
//
//        }
//        oldOrderRepository.saveAll(oldOrderList);
//    }

    @Override
    public List<OldOrder> findByUserId(String userId) {
        return oldOrderRepository.findByUserId(userId);
    }

    @Override
    public Page<OldOrder> findAll(Pageable pageable) {
        return oldOrderRepository.findAll(pageable);
    }
}
