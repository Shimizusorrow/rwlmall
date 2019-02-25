package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/16 10:21
 */

@Repository
public interface AgentRepository extends JpaRepository<Agent,String> {

    Agent findByAgentName(String agentName);
}
