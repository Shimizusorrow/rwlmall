package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Agent;
import com.qunchuang.rwlmall.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/4/17 10:47
 */

public interface StoreService {

    /*保存代理商*/
    Store save(Store store);

    /*修改*/
    Store update(String storeId, Store store);

    /*通过id 查找*/
    Store findOne(String storeId);

    /*删除一家门店*/
    void delete(String storeId);

    /*查找代理商所属门店*/
    Page<Store> findByAgentId(String agentId, Pageable pageable);

    /*按区域查找门店*/
    List<Store> findByProvinceContainingAndCityContainingAndAreaContaining(String province, String city, String area);

    /*代理商按区域查找门店*/
    List<Store> findByAgentIdAndProvinceContainingAndCityContainingAndAreaContaining(String agentId,String province, String city, String area);

    /*查找所有*/
    List<Store> findAll();

    /*门店是否存在*/
    boolean existStore(String storeId);

    /*门店状态切换*/
    void modifyStatus(String storeId);

    /*可派门店*/
    List<Store> findDistributeStore(Principal principal);

    /*通过状态查找 AND 门店已有账号*/
    List<Store> findByStatus(Integer status);

    /*通过状态+代理商查找 AND 门店已有账号*/
    List<Store> findByAgentIdAndStatus(String agentId, Integer status);

}
