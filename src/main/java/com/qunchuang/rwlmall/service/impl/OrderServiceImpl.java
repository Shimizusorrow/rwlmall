package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Agent;
import com.qunchuang.rwlmall.domain.MallOrder;
import com.qunchuang.rwlmall.domain.Order;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.enums.OrderStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.RoleEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.OrderRepository;
import com.qunchuang.rwlmall.service.AgentService;
import com.qunchuang.rwlmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/8 15:59
 */

public class OrderServiceImpl<T> implements OrderService<T> {

    @Autowired
    private OrderRepository<T> orderRepository;

    @Autowired
    AgentService agentService;

    @Override
    public Page<T> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<T> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public T findOne(String orderId) {
        Optional<T> optional = orderRepository.findById(Optional.ofNullable(orderId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public Page<T> findByPhone(String phone, Pageable pageable) {
        return orderRepository.findByPhoneContaining(phone, pageable);
    }

    @Override
    public List<T> findByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<T> findByStatus(Integer status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }


    @Override
    public List<T> findByTimeAndPayStatusAndPayMode(Long startTime,Long endTime,Integer payStatus,Integer payMode) {
        return orderRepository.findByCreatetimeBetweenAndPayStatusAndPayMode(startTime,endTime,payStatus,payMode);
    }

    @Override
    public List<T> findByTimeAndPayStatusAndStatus(Long startTime, Long endTime, Integer payStatus, Integer status) {
        return orderRepository.findByCreatetimeBetweenAndPayStatusAndStatus(startTime, endTime, payStatus, status);
    }

    @Override
    public Page<T> findByStatusAndArea(Integer status, String province, String city, String area, Pageable pageable, Principal principal) {

        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            Agent agent = agentService.findOne(role.getAccountId());

            if (agent.getRegionDistribution().contains(area)&&agent.getRegion().contains(city)) {
                return orderRepository.findByStatusAndProvinceContainingAndCityContainingAndAreaContaining(status, province, city, area, pageable);
            }
            throw new AccessDeniedException("权限不足");

        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //总部
            return orderRepository.findByStatusAndProvinceContainingAndCityContainingAndAreaContaining(status, province, city, area, pageable);
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public Page<T> finByOrderTime(Long startTime, Long endTime, Pageable pageable) {
        return orderRepository.findByCreatetimeBetween(startTime, endTime, pageable);
    }

    @Override
    public T findByNumber(String number) {
        Optional<T> optional = orderRepository.findByNumber(Optional.ofNullable(number).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_NOT_EXIST);
        }
        return optional.get();
    }


}
