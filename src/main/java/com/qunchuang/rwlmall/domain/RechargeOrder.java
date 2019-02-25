package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.BosSet;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.bos.IBosSet;
import com.qunchuang.rwlmall.enums.OrderStatusEnum;
import com.qunchuang.rwlmall.enums.PayModeEnum;
import com.qunchuang.rwlmall.enums.PayStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Curtain
 * @date 2018/4/16 9:58
 */
@Entity
@Bostype("A15")
@Table(name = "T_RechargeOrder")
@Getter
@Setter
public class RechargeOrder extends BaseEntity implements Order{

    /*用户id*/
    @Column(length = 40)
    private String userId;

    /*充值金额*/
    private long payMoney;

    /*奖励金额*/
    private long rewardMoney;

    /*支付状态*/
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    @Override
    public long getAmount() {
        return payMoney;
    }

    @Override
    public Integer getStatus() {
        return null;
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public long getCardAmount() {
        return 0L;
    }

    @Override
    public void setCardAmount(long cardAmount) {

    }
}
