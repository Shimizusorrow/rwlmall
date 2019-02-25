package com.qunchuang.rwlmall.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/5/10 9:14
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void existsByOpenid() throws Exception {

        System.out.println(userRepository.existsByOpenid("oCiAMj1oH5gloORumtL8Ay-nlo8k"));
    }

}