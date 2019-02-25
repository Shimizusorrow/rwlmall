package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/3/12 15:21
 */

@Entity
@Bostype("A06")
@Table(name = "T_Advertisement")
@Getter
@Setter
public class Advertisement extends BaseEntity{

    /*所属类别*/
    private Integer type;

    /*图片路径*/
    @Column(length = 1000)
    private String image;

    /*轮播时间*/
    private long time;

    /*广告跳转网址*/
    @Column(length = 1000)
    private String webAddress;


}


