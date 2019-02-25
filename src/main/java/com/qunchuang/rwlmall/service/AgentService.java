package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/16 10:22
 */
public interface AgentService {

    Agent findByAgentName(String agentName);

    /*保存代理商*/
    Agent save(Agent agent);

    /*修改*/
    Agent update(String agentId, Agent agent);

    /*通过id 查找*/
    Agent findOne(String agentId);

    /*删除一家代理商*/
    void delete(String agentId);

    /*查找所有*/
    Page<Agent> findAll(Pageable pageable);

    /*代理商是否存在*/
    boolean existAgent(String agentId);

    /*查找所有*/
    List<Agent> findAll();

    /*判断区域是否重复*/
    boolean existAgentRegionDistribution(Agent agent,String agentId);
}
