package com.qunchuang.rwlmall.service.impl;

import com.qunchuang.rwlmall.bean.UserRecord;
import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.enums.UserRecordEnum;
import com.qunchuang.rwlmall.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/13 9:13
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAll() throws Exception {
        User user = userRepository.findByCountryContainingAndPhoneContaining("", "181");
        System.out.println(user);
    }

    @Test
    public void findAll1() throws Exception {
    }

    @Test
    public void findOne() throws Exception {
        User user = userService.findOne("w-MRtUJIHIKDB3bqgiwMv0A02");
        user.setBalance(new Long(1000));
        user.setFrequency(10);
        userService.save(user);
        Assert.assertNotNull(user);
    }

    @Test
    public void findByOpenid() throws Exception {
        Assert.assertNotNull(userService.findByOpenid("OMihuHDUHAOHoaoiOSA"));
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void save() throws Exception {
        User user = new User();
        user.setName("测试数据");
        user.setPhone("18157726283");
        user.setOpenid("OMihuHDUHAOHoaoiOSA");
        user.setGender("男");
        user.setCountry("中国");
        Assert.assertNotNull(userService.save(user));
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void update() throws Exception {
        User user = new User();
        user.setName("测试数据2");
        Assert.assertNotNull(userService.update("OMihuHDUHAOHoaoiOSA", user));

    }

    @Test
    public void recharge() throws Exception {
    }

    @Test
    public void count() throws Exception {
        Integer count = userService.count();
        System.out.println(count);
    }

    @Test
    public void page() {
        Page<User> page = userService.findAll(PageRequest.of(7, 6));
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        int k = 0;

        for (int j = 0; j < page.getContent().size(); j++) {
            User user = page.getContent().get(j);
            k++;
            System.out.println(user);
        }
        System.out.println(k);
        System.out.println(page);
    }
}