package com.qunchuang.rwlmall.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 财务收支统计
 * @author Curtain
 * @date 2018/7/17 9:45
 */

@Getter
@Setter
public class FinanceStatistical {

    /**
     * 微信总收入
     */
    private Long weChatIncome;

    /**
     * 充值总金额
     */
    private Long rechargeIncome;

    /**
     * 订单微信支付总额
     */
    private Long weChatAmount;

    /**
     * 账户总余额
     */
    private Long accountBalance;

    /**
     * 退款总额
     */
    private Long refundAmount;

    /**
     * 充值奖励总额
     */
    private Long rewardAmount;

    /**
     *余额消费总额
     */
    private Long balanceAmount;

    /**
     * 消费总额
     */
    private Long consumeAmount;

    /**
     * 会员卡消费总额
     */
    private Long cardAmount;

    public FinanceStatistical(Long weChatIncome, Long rechargeIncome, Long weChatAmount, Long accountBalance, Long refundAmount, Long rewardAmount, Long balanceAmount, Long consumeAmount, Long cardAmount) {
        this.weChatIncome = weChatIncome;
        this.rechargeIncome = rechargeIncome;
        this.weChatAmount = weChatAmount;
        this.accountBalance = accountBalance;
        this.refundAmount = refundAmount;
        this.rewardAmount = rewardAmount;
        this.balanceAmount = balanceAmount;
        this.consumeAmount = consumeAmount;
        this.cardAmount = cardAmount;
    }
}
