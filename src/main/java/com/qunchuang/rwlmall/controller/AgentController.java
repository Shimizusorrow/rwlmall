package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.domain.Agent;
import com.qunchuang.rwlmall.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author Curtain
 * @date 2018/3/13 15:31
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/agent")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AgentController {

    @Autowired
    private AgentService agentService;

    /*通过代理商id查询*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("agentid") String agentId){
        return agentService.findOne(agentId);
    }

    /*保存*/
    @PostMapping("/save")
    public Object save(@RequestBody Agent agent){
        return agentService.save(agent);
    }

    /*修改代理商信息*/
    @PostMapping("/update")
    public Object update(@RequestParam("agentid") String agnetId,@RequestBody Agent agent){
        return agentService.update(agnetId, agent);
    }

    /*查找所有*/
    @RequestMapping("/findall")
    public Object findAll(@RequestParam(name = "size", defaultValue = "8") Integer size,
                          @RequestParam(name = "page", defaultValue = "1") Integer page){

        Pageable pageable = PageRequest.of(page-1,size);
        return agentService.findAll(pageable);
    }

}
