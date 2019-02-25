package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 自定义分页对象
 * @author Curtain
 * @date 2018/7/6 9:26
 */

@Getter
@Setter
public class MyPage {

    /*总大小*/
    private Integer totalSize;

    /*总页数*/
    private Integer totalPage;

    /*当前页*/
    private Integer currentPage;

    /*data*/
    private Object object;
}
