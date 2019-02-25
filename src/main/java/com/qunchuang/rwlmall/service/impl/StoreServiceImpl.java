package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Agent;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.domain.Store;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.RoleEnum;
import com.qunchuang.rwlmall.enums.StoreStatusEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.StoreRepository;
import com.qunchuang.rwlmall.service.AgentService;
import com.qunchuang.rwlmall.service.StoreService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/4/17 10:50
 */

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AgentService agentService;

    @Override
    public Store save(Store store) {
        if (!Optional.ofNullable(store).isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_INFO_NOT_TRUE);
        }
        return storeRepository.save(store);
    }

    @Override
    public Store update(String storeId, Store store) {
        Store result = findOne(storeId);
        BeanUtils.copyProperties(store, result, BeanCopyUtil.getNullPropertyNames(store));
        return storeRepository.save(result);
    }

    @Override
    public Store findOne(String storeId) {
        Optional<Store> optional = storeRepository.findById(Optional.ofNullable(storeId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public void delete(String storeId) {
        Store result = findOne(storeId);
        storeRepository.delete(result);
    }

    @Override
    public Page<Store> findByAgentId(String agentId, Pageable pageable) {
        return storeRepository.findByAgentId(agentId, pageable);
    }

    @Override
    public List<Store> findByStatus(Integer status) {
        return storeRepository.findByStatusAndAccount(status,true);
    }

    @Override
    public List<Store> findByAgentIdAndStatus(String agentId, Integer status) {
        return storeRepository.findByAgentIdAndStatusAndAccount(agentId, status,true);
    }

    @Override
    public List<Store> findDistributeStore(Principal principal) {
        Role role = (Role) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        /*如果是代理商*/
        if (role.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            return findByAgentIdAndStatus(role.getAccountId(), StoreStatusEnum.BUSINESS.getCode());
        } else if (role.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) || role.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //总部
            return findByStatus(StoreStatusEnum.BUSINESS.getCode());
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public void modifyStatus(String storeId) {
        Store store = findOne(storeId);
        if (store.getStatus().equals(StoreStatusEnum.BUSINESS.getCode())) {
            store.setStatus(StoreStatusEnum.UN_BUSINESS.getCode());
        } else if (store.getStatus().equals(StoreStatusEnum.UN_BUSINESS.getCode())) {
            store.setStatus(StoreStatusEnum.BUSINESS.getCode());
        } else {
            store.setStatus(StoreStatusEnum.UN_BUSINESS.getCode());
        }
        storeRepository.save(store);
    }

    @Override
    public boolean existStore(String storeId) {
        return storeRepository.existsById(storeId);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> findByAgentIdAndProvinceContainingAndCityContainingAndAreaContaining(String agentId, String province, String city, String area) {
        return storeRepository.findByAgentIdAndProvinceContainingAndCityContainingAndAreaContaining(agentId,province,city,area);
    }

    @Override
    public List<Store> findByProvinceContainingAndCityContainingAndAreaContaining(String province, String city, String area) {
        return storeRepository.findByProvinceContainingAndCityContainingAndAreaContaining(province, city, area);
    }
}
