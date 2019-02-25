package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/4/28 14:32
 */

@Entity
@Bostype("A20")
@Table(name = "T_PlatformText")
@Getter
@Setter
public class PlatformText extends BaseEntity {

    /*内容*/
    @Column(length = 8128)
    private String content;

    /*类型*/
    private Integer type;
}
