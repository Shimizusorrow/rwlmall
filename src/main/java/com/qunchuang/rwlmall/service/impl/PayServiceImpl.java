package com.qunchuang.rwlmall.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.qunchuang.rwlmall.bean.WeChatShare;
import com.qunchuang.rwlmall.config.WeChatAccountConfig;
import com.qunchuang.rwlmall.domain.*;
import com.qunchuang.rwlmall.enums.OrderCategoryEnum;
import com.qunchuang.rwlmall.enums.PayModeEnum;
import com.qunchuang.rwlmall.enums.PayStatusEnum;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.service.*;
import com.qunchuang.rwlmall.utils.AmountUtil;
import com.qunchuang.rwlmall.utils.CardPayUtil;
import com.qunchuang.rwlmall.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Curtain
 * @date 2018/4/12 10:13
 */

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private UserService userService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private LaundryOrderService laundryOrderService;

    @Autowired
    private FurnitureOrderService furnitureOrderService;

    @Autowired
    private RechargeOrderService rechargeOrderService;

    @Autowired
    private MallProductService mallProductService;

    @Autowired
    private FurnitureProductService furnitureProductService;

    @Autowired
    private LaundryProductService laundryProductService;

    @Autowired
    private WeChatAccountConfig weChatAccountConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  cardPay(String orderId, String userId) {
        String key = orderId.substring(orderId.length() - 3, orderId.length());
        if (OrderCategoryEnum.RECHARGE_ORDER.getKey().equals(key)) {
            throw new RwlMallException(ResultExceptionEnum.NOT_CARD_PAY);
        }

        Order order = disOrderCategorySearch(orderId);

        //判断支付状态
        if (!(order.getPayStatus().equals(PayStatusEnum.WAIT.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_PAY_STATUS_ERROR);
        }

        //卡折扣计算
//        cardPayDiscountCalc(orderId);

        //支付订单
        User user = userService.findOne(order.getUserId());

        CardPayUtil.pay(user.getCid(), user.getCno(), String.valueOf(AmountUtil.fenToYuan(order.getAmount())));

        disOrderCategoryPaid(orderId, PayModeEnum.CARD.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void balancePay(String orderId) {

        String key = orderId.substring(orderId.length() - 3, orderId.length());
        if (OrderCategoryEnum.RECHARGE_ORDER.getKey().equals(key)) {
            throw new RwlMallException(ResultExceptionEnum.NOT_BALANCE_PAY);
        }


        Order order = disOrderCategorySearch(orderId);

        //判断支付状态
        if (!(order.getPayStatus().equals(PayStatusEnum.WAIT.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_PAY_STATUS_ERROR);
        }

        //找到用户判断余额是否足够
        User user = userService.findOne(order.getUserId());

        if ((user.getBalance() - order.getAmount()) < 0) {
            throw new RwlMallException(ResultExceptionEnum.BALANCE_NOT_ENOUGH);
        }

        //余额充足  支付订单
        user.setBalance(user.getBalance() - order.getAmount());
        userService.save(user);

        disOrderCategoryPaid(orderId, PayModeEnum.BALANCE.getCode());
    }

    @Override
    public PayResponse weChatPay(String orderId) {

        PayRequest payRequest = new PayRequest();

        /*根据ID后三位判断 订单类别*/
        Order order = disOrderCategorySearch(orderId);

        //判断支付状态
        if (!(order.getPayStatus().equals(PayStatusEnum.WAIT.getCode()))) {
            throw new RwlMallException(ResultExceptionEnum.ORDER_PAY_STATUS_ERROR);
        }

        String openid = userService.findOne(order.getUserId()).getOpenid();
        payRequest.setOpenid(openid);
        payRequest.setOrderAmount(new Long(order.getAmount()).intValue());
        payRequest.setOrderId(order.getId());
        payRequest.setOrderName("让我来");
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        return bestPayService.pay(payRequest);
    }

    @Override
    public WeChatShare generate() {
        WeChatShare weChatShare = new WeChatShare();

        weChatShare.setAppid(weChatAccountConfig.getAppId());
        weChatShare.setTimestamp(String.valueOf(System.currentTimeMillis()));
        weChatShare.setNonceStr(RandomUtil.getRandomStr());

        String sign = "appId="+weChatShare.getAppid()+"&nonceStr="+weChatShare.getNonceStr()+"&timestamp="+weChatShare.getTimestamp();
        //todo 支付排除
//        String signature = DigestUtils.sha1Hex(sign);
//        weChatShare.setSignature(signature);

        return weChatShare;
    }

    @Override
    public PayResponse notify(String notifyData) {
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        //取消判断金额  因为微信支付签名验证已经保证了正确性
//        Order order = disOrderCategorySearch(payResponse.getOrderId());
//        if (!(new Long(order.getAmount()).intValue() - (payResponse.getOrderAmount()) == 0)) {
//            throw new RwlMallException(ResultExceptionEnum.AMOUNT_NOT_TRUE);
//        }
        disOrderCategoryPaid(payResponse.getOrderId(), PayModeEnum.WE_CHAT.getCode());


        return payResponse;
    }


    /*订单分类别查找*/
    private Order disOrderCategorySearch(String orderId) {
        Order order = null;
        String key = orderId.substring(orderId.length() - 3, orderId.length());
        if (OrderCategoryEnum.MALL_ORDER.getKey().equals(key)) {
            order = mallOrderService.findOne(orderId);
            /*判断库存是否充足*/
            Boolean judgeStock = mallProductService.judgeStock((MallOrder) order);
            if (judgeStock) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
            }
        }
        if (OrderCategoryEnum.LAUNDRY_ORDER.getKey().equals(key)) {
            order = laundryOrderService.findOne(orderId);
            /*判断库存是否充足*/
            Boolean judgeStock = laundryProductService.judgeStock((LaundryOrder) order);
            if (judgeStock) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
            }
        }
        if (OrderCategoryEnum.FURNITURE_ORDER.getKey().equals(key)) {
            order = furnitureOrderService.findOne(orderId);
            /*判断库存是否充足*/
            Boolean judgeStock = furnitureProductService.judgeStock((FurnitureOrder) order);
            if (judgeStock) {
                throw new RwlMallException(ResultExceptionEnum.PRODUCT_STOCK_ERROR);
            }
        }
        if (OrderCategoryEnum.RECHARGE_ORDER.getKey().equals(key)) {
            order = rechargeOrderService.findOne(orderId);
        }

        return order;
    }

    /*订单分类别支付*/
    private void disOrderCategoryPaid(String orderId, Integer payMode) {
        String key = orderId.substring(orderId.length() - 3, orderId.length());
        if (OrderCategoryEnum.MALL_ORDER.getKey().equals(key)) {
            mallOrderService.paid(orderId, payMode);
        }
        if (OrderCategoryEnum.LAUNDRY_ORDER.getKey().equals(key)) {
            laundryOrderService.paid(orderId, payMode);
        }
        if (OrderCategoryEnum.FURNITURE_ORDER.getKey().equals(key)) {
            furnitureOrderService.paid(orderId, payMode);
        }
        if (OrderCategoryEnum.RECHARGE_ORDER.getKey().equals(key)) {
            rechargeOrderService.paid(orderId, payMode);
        }

    }

    /*卡支付折扣金额计算*/
    private Order cardPayDiscountCalc(String orderId) {
        Order order = null;
        String key = orderId.substring(orderId.length() - 3, orderId.length());
        if (OrderCategoryEnum.MALL_ORDER.getKey().equals(key)) {
            order = mallOrderService.findOne(orderId);
            User user = userService.findOne(order.getUserId());
            long cardAmount = order.getAmount() * user.getDiscount() / 100;
            order.setCardAmount(cardAmount);
            mallOrderService.save((MallOrder) order);

        }
        if (OrderCategoryEnum.LAUNDRY_ORDER.getKey().equals(key)) {
            order = laundryOrderService.findOne(orderId);
            User user = userService.findOne(order.getUserId());
            long cardAmount = order.getAmount() * user.getDiscount() / 100;
            order.setCardAmount(cardAmount);
            laundryOrderService.save((LaundryOrder) order);
        }
        if (OrderCategoryEnum.FURNITURE_ORDER.getKey().equals(key)) {
            order = furnitureOrderService.findOne(orderId);
            User user = userService.findOne(order.getUserId());
            long cardAmount = order.getAmount() * user.getDiscount() / 100;
            order.setCardAmount(cardAmount);
            furnitureOrderService.save((FurnitureOrder) order);
        }
        return order;
    }


    /*
    //微信退款
    @Override
    public RefundResponse refund(String orderId) {
        RefundRequest refundRequest = new RefundRequest();
        Order order = disOrderCategorySearch(orderId);
        refundRequest.setOrderId(order.getId());
        refundRequest.setOrderAmount(order.getAmount().intValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        RefundResponse refundResponse = bestPayService.refund(refundRequest);

        return refundResponse;
    }
*/

}
