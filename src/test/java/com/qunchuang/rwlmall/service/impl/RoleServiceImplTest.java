package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.domain.Role;
import com.qunchuang.rwlmall.enums.RoleEnum;
import com.qunchuang.rwlmall.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/3/19 9:40
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void save() throws Exception {
        Role role = new Role();
        role.setUsername("admin");
        role.setPassword("1");
        role.setRoleAuthority(RoleEnum.GENERAL_HQ_ROLE.getMessage());
        roleService.saveGeneralHQ(role);
//        Assert.assertNotNull(result);
    }

    @Test
    public void modifyPassword() throws Exception {
//        Role result = roleService.modifyPassword("admin", "admin");
//        Assert.assertNotNull(result);
    }

    @Test
    public void delete() throws Exception {
    }

}