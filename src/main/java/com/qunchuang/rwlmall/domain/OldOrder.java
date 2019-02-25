package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.BosSet;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.IBosSet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 旧数据的订单
 *
 * @author Curtain
 * @date 2018/9/3 9:07
 */

@Entity
@Bostype("A27")
@Table(name = "T_OldOrder")
@Getter
@Setter
public class OldOrder extends BaseEntity implements Order {

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人
     */
    private String name;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单金额
     */
    private Long amount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 支付方式
     */
    private String payMode;

    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    @OneToMany(cascade
            = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<OldOrderItem> items = new HashSet<>();

    public IBosSet<OldOrderItem> getItems() {
        return new BosSet(this.items, this);
    }

    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    public void setItems(Set<OldOrderItem> items) {
        this.items = items;
    }

    @Override
    public Integer getPayStatus() {
        return null;
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public long getCardAmount() {
        return 0;
    }

    @Override
    public void setCardAmount(long cardAmount) {

    }

    @Override
    public Integer getStatus() {
        return null;
    }

    @Override
    public long getAmount() {
        return amount;
    }
}
