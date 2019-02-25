package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Agent;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.AgentRepository;
import com.qunchuang.rwlmall.service.AgentService;
import com.qunchuang.rwlmall.utils.BeanCopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/16 10:22
 */

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public Agent findByAgentName(String agentName) {
        return agentRepository.findByAgentName(agentName);
    }

    @Override
    public Agent save(Agent agent) {
        if (!Optional.ofNullable(agent).isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_INFO_NOT_TRUE);
        }

        if (existAgentRegionDistribution(agent,null)) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_EXIST_REGION_DISTRIBUTION);
        }

        return agentRepository.save(agent);
    }

    @Override
    public Agent update(String agentId, Agent agent) {
        if (existAgentRegionDistribution(agent,agentId)) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_EXIST_REGION_DISTRIBUTION);
        }


        Agent result = findOne(agentId);
        boolean isAccount = result.isAccount();
        BeanUtils.copyProperties(agent, result, BeanCopyUtil.getNullPropertyNames(agent));
        result.setAccount(isAccount);
        return agentRepository.save(result);
    }

    @Override
    public Agent findOne(String agentId) {
        Optional<Agent> optional = agentRepository.findById(Optional.ofNullable(agentId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_NOT_EXIST);
        }
        return optional.get();
    }

    @Override
    public boolean existAgentRegionDistribution(Agent agent,String agentId) {
        //判断区域是否重复
        List<Agent> agentList = findAll();
        for (Agent a : agentList) {
            if (a.getId().equals(agent.getId())){
                continue;
            }
            if(a.getId().equals(agentId)){
                continue;
            }
            String[] strings = agent.getRegionDistribution().split(",");
            if (a.getRegion().equals(agent.getRegion())){
                for (String s :strings){
                    if (a.getRegionDistribution().contains(s)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    @Override
    public boolean existAgent(String agentId) {
        return agentRepository.existsById(agentId);
    }

    @Override
    public Page<Agent> findAll(Pageable pageable) {
        return agentRepository.findAll(pageable);
    }

    @Override
    public void delete(String agentId) {
        Agent result = findOne(agentId);
        agentRepository.delete(result);
    }

}
