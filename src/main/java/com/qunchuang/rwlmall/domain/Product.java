package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/4/11 8:04
 */

public interface Product {

    Integer getStatus();

    int getSort();

    void setStatus(Integer status);

    void setAutoStatus(boolean autoStatus);
}
