package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.RoleVO;
import com.qunchuang.rwlmall.domain.Agent;
import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.domain.Store;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.enums.RoleEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.repository.RoleRepository;
import com.qunchuang.rwlmall.service.AgentService;
import com.qunchuang.rwlmall.service.RoleService;
import com.qunchuang.rwlmall.service.StoreService;
import com.qunchuang.rwlmall.utils.ConvertUtil;
import com.qunchuang.rwlmall.utils.MD5Util;
import com.qunchuang.rwlmall.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/19 9:11
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AgentService agentService;

    @Autowired
    private StoreService storeService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Role> optional = roleRepository.findByUsername(Optional.ofNullable(s).orElse(""));
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException(ResultExceptionEnum.ROLE_IS_EXIST.getMessage());
        }
        return optional.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAgent(Role role) {
        usernamePasswordJudge(role);

        //代理商是否存在
        if (!agentService.existAgent(role.getAccountId())) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_NOT_EXIST);
        }

        Agent agent = agentService.findOne(role.getAccountId());

        //代理商账号是否存在
        if (agent.isAccount()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_IS_EXIST);
        } else {
            agent.setAccount(true);
            agent.setAccountName(role.getUsername());
        }

        role.setRoleAuthority(RoleEnum.AGENT_ROLE.getMessage());
        role.setRoleNumber(RoleEnum.AGENT_ROLE.getCode());
        //代理商默认权限
        role.setFunctionAuthority(RoleUtil.AGENT_ROLE);

        agentService.save(agent);

        roleRepository.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStore(Role role) {
        usernamePasswordJudge(role);

        //门店是否存在
        if (!storeService.existStore(role.getAccountId())) {
            throw new RwlMallException(ResultExceptionEnum.STORE_NOT_EXIST);
        }

        Store store = storeService.findOne(role.getAccountId());

        //门店账号是否存在
        if (store.isAccount()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_IS_EXIST);
        } else {
            store.setAccount(true);
            store.setAccountName(role.getUsername());
        }

        role.setRoleAuthority(RoleEnum.STORE_ROLE.getMessage());
        role.setRoleNumber(RoleEnum.STORE_ROLE.getCode());
        //门店默认权限
        role.setFunctionAuthority(RoleUtil.STORE_ROLE);

        storeService.save(store);
        roleRepository.save(role);
    }

    @Override
    public void saveGeneralAgent(Role role) {
        usernamePasswordJudge(role);

        //代理商是否存在
        if (!agentService.existAgent(role.getAccountId())) {
            throw new RwlMallException(ResultExceptionEnum.AGENT_NOT_EXIST);
        }

        role.setRoleAuthority(RoleEnum.GENERAL_AGENT_ROLE.getMessage());
        role.setRoleNumber(RoleEnum.GENERAL_AGENT_ROLE.getCode());

        roleRepository.save(role);
    }

    @Override
    public void saveGeneralHQ(Role role) {
        usernamePasswordJudge(role);

        role.setRoleAuthority(RoleEnum.GENERAL_HQ_ROLE.getMessage());
        role.setRoleNumber(RoleEnum.GENERAL_HQ_ROLE.getCode());

        roleRepository.save(role);

    }

    private void usernamePasswordJudge(Role role) {
        if (role.getUsername() == null || role.getUsername() == "" || role.getPassword() == null || role.getPassword() == "") {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_TRUE);
        }

        if (roleRepository.existsByUsername(role.getUsername())) {
            //如果存在 则返回用户已存在
            throw new RwlMallException(ResultExceptionEnum.ROLE_IS_EXIST);
        }
        role.setPassword(MD5Util.generate(role.getPassword()));
    }

    @Override
    public Boolean modifyPassword(String username, String password, Principal principal) {
        Optional<Role> loginOptional = roleRepository.findByUsername(principal.getName());
        Optional<Role> modifyOptional = roleRepository.findByUsername(Optional.ofNullable(username).orElse(""));
        if (!modifyOptional.isPresent() || !loginOptional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_EXIST);
        }

        Role loginRole = loginOptional.get();
        Role modifyRole = modifyOptional.get();

        if (modifyRole.getPassword() == null || modifyRole.getPassword() == "") {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_TRUE);
        }

        //不同角色  不同的修改密码权限
        if (loginRole.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode())) {
            //系统管理员可以修改所有人密码
            modifyRole.setPassword(MD5Util.generate(password));
        } else if (loginRole.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode())) {
            //代理商管理员  可以修改自身 和 修改旗下角色、门店密码
            if (modifyRole.getAccountId().equals(loginRole.getAccountId())) {
                modifyRole.setPassword(MD5Util.generate(password));
            } else {
                throw new RwlMallException(ResultExceptionEnum.AUTHENTICATION_FAIL);
            }
        } else {
            //其他权限 可以修改自身的密码
            if (loginRole.getUsername().equals(modifyRole.getUsername())) {
                modifyRole.setPassword(MD5Util.generate(password));
            }
        }
        roleRepository.save(modifyRole);

        if (loginRole.getUsername().equals(modifyRole.getUsername())) {
            //自身修改密码 则要注销 重新登录
            return true;
        }

        return false;
    }

    @Override
    public List<RoleVO> findByRoleNumber(Integer number) {
        List<RoleVO> resultList = new ArrayList<>();
        List<Role> roleList = roleRepository.findByRoleNumber(number);

        roleList.forEach(role -> resultList.add(ConvertUtil.convert(role)));

        return resultList;
    }

    @Override
    public RoleVO findOne(String roleId) {
        Optional<Role> optional = roleRepository.findById(Optional.ofNullable(roleId).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_EXIST);
        }
        return ConvertUtil.convert(optional.get());
    }

    @Override
    public List<RoleVO> findByRoleNumberAndAccountId(Integer number, String accountId) {
        List<RoleVO> resultList = new ArrayList();
        List<Role> roleList = roleRepository.findByRoleNumberAndAccountId(number, accountId);

        roleList.forEach(role -> resultList.add(ConvertUtil.convert(role)));

        return resultList;
    }

    @Override
    public void modifyPermission(String username, String permission, Principal principal) {
        Optional<Role> loginOptional = roleRepository.findByUsername(principal.getName());
        Optional<Role> modifyOptional = roleRepository.findByUsername(Optional.ofNullable(username).orElse(""));
        if (!modifyOptional.isPresent() || !loginOptional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_EXIST);
        }

        Role loginRole = loginOptional.get();
        Role modifyRole = modifyOptional.get();

        /*系统管理员 或者 代理商  修改角色权限*/
        if (loginRole.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode()) && modifyRole.getRoleNumber().equals(RoleEnum.GENERAL_HQ_ROLE.getCode())) {
            //系统管理员修改 总部管理员权限
            if (RoleUtil.authorityJudge(RoleUtil.GENERAL_HQ_ROLE, permission)) {
                //判断修改后  是否是该角色拥有的最大权限子集    修改权限
                modifyRole.setFunctionAuthority(permission);
            } else {
                throw new RwlMallException(ResultExceptionEnum.ROLE_UPDATE_FAIL);
            }
        } else if (loginRole.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) && modifyRole.getRoleNumber().equals(RoleEnum.GENERAL_AGENT_ROLE.getCode())) {
            //代理商管理员修改  代理商旗下角色权限
            if (RoleUtil.authorityJudge(RoleUtil.GENERAL_AGENT_ROLE, permission)) {
                //判断修改后  是否是该角色拥有的最大权限子集  修改权限
                modifyRole.setFunctionAuthority(permission);
            } else {
                throw new RwlMallException(ResultExceptionEnum.ROLE_UPDATE_FAIL);
            }
        } else {
            throw new RwlMallException(ResultExceptionEnum.AUTHENTICATION_FAIL);
        }
    }


//    @Override
//    public void initAdmin() {
//        Role role = new Role();
//        role.setPassword(MD5Util.generate("1"));
//        role.setUsername("admin");
//        role.setFunctionAuthority("A1,A2,A3,A4,A5,B1,B2,B3,C1,C2,C3,C4,C5,D1,D2,D3,E1,E2,E3,E4,E5");
//        role.setRoleNumber(1);
//        role.setRoleAuthority("ROLE_ADMIN");
//        roleRepository.save(role);
//    }

    @Override
    public RoleVO getSelfInfo(Principal principal) {
        Optional<Role> optional = roleRepository.findByUsername(Optional.ofNullable(principal.getName()).orElse(""));
        if (!optional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_TRUE);
        }
        Role role = optional.get();

        return ConvertUtil.convert(role);

    }

    @Override
    public void delete(String username, Principal principal) {

        Optional<Role> loginOptional = roleRepository.findByUsername(principal.getName());
        Optional<Role> modifyOptional = roleRepository.findByUsername(Optional.ofNullable(username).orElse(""));
        if (!modifyOptional.isPresent() || !loginOptional.isPresent()) {
            throw new RwlMallException(ResultExceptionEnum.ROLE_NOT_EXIST);
        }

        Role loginRole = loginOptional.get();
        Role modifyRole = modifyOptional.get();

        if (loginRole.getRoleNumber().equals(RoleEnum.ADMIN_ROLE.getCode())) {
            roleRepository.delete(modifyRole);
        } else if (loginRole.getRoleNumber().equals(RoleEnum.AGENT_ROLE.getCode()) && loginRole.getAccountId().equals(modifyRole.getAccountId())) {
            roleRepository.delete(modifyRole);
        } else {
            throw new RwlMallException(ResultExceptionEnum.AUTHENTICATION_FAIL);
        }

    }
}
