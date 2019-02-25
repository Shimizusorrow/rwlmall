package com.qunchuang.rwlmall.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Curtain
 * @date 2018/3/21 15:20
 */
public class MD5UtilTest {

    @Test
    public void test(){
        String password = "1";

        String generate = MD5Util.generate(password);

        System.out.println(generate);

        System.out.println(MD5Util.verify("22222222","763407954209433f8a88b49e82087041121619571986c53c"));
    }


    @Test
    public void test2(){
        System.out.println(Double.valueOf(1234)/100.0);

        String id = "tp4TDBvnHKCvgvFY9FyGm1A08";

        if (id.endsWith("A08")){
            id = id.substring(0,id.lastIndexOf("A08"));
            id +="A27";
        }

        System.out.println(id);

    }



}