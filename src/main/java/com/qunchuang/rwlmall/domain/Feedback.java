package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/4/25 8:37
 * 用户反馈
 */

@Entity
@Bostype("A18")
@Table(name = "T_Feedback")
@Getter
@Setter
public class Feedback extends BaseEntity {

    /*反馈内容*/
    private String content;

    /*姓名*/
    private String name;

    /*会员id number*/
    private String memberId;

    /*用户手机号*/
    private String phone;


}
