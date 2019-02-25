package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.BosSet;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.IBosSet;
import com.qunchuang.rwlmall.enums.OrderStatusEnum;
import com.qunchuang.rwlmall.enums.PayStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/4/8 9:14
 */

@SuppressWarnings("ALL")
@Entity
@Bostype("A13")
@Table(name = "T_FurnitureOrder")
@Getter
@Setter
public class FurnitureOrder extends BaseEntity implements Order{

    /*姓名*/
    @Column(length = 20)
    private String name;

    /*手机号*/
    @Column(length = 20)
    private String phone;

    /*详细地址*/
    @Column(length = 100)
    private String address;

    /*省份*/
    @Column(length = 20)
    private String province;

    /*城市*/
    @Column(length = 20)
    private String city;

    /*区域*/
    @Column(length = 20)
    private String area;

    /*所属门店id*/
    @Column(length = 40)
    private String storeId;

    /*用户id*/
    @Column(length = 40)
    private String userId;

    /*总金额*/
    private long amount;

    /*卡支付金额*/
    private long cardAmount;

    /*取货日期*/
    private String deliveryDate;

    /*备注*/
    private String remark;

    /*订单状态*/
    private Integer status = OrderStatusEnum.NEW.getCode();

    /*支付状态*/
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /*付款方式*/
    private Integer payMode;

    /*代理商id*/
    @Column(length = 40)
    private String agentId;

    /*是否入站*/
    private boolean inbound;

    /*收单人员*/
    private String receiptPeople;

    /*服务商户*/
    private String serviceStore;

    /*用户openid*/
    private String openid;

    /*订单状态更新时间*/
    private String statusUpdateTime;

    /*订单完结时间*/
    @Column(name="finish_time",length=40)
    private long finishTime;

    /*注册手机号*/
    private String registerPhone;

    /*绑定手机号*/
    private String bindPhone;

    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    @OneToMany(cascade
            = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FurnitureOrderItem> items = new HashSet<>();

    public IBosSet<FurnitureOrderItem> getItems() {
        return new BosSet(this.items, this);
    }

    @SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
    public void setItems(Set<FurnitureOrderItem> items){
        this.items = items;
    }
}
