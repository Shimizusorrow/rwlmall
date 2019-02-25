package com.qunchuang.rwlmall.enums;

import lombok.Getter;

@Getter
public enum ResultExceptionEnum {

    PRODUCT_NOT_EXIST(10001, "商品不存在"),
    PRODUCT_NOT_UP(10005,"商品已下架"),
    PRODUCT_STOCK_ERROR(10002, "库存不足"),
    PRODUCT_INFO_NOT_TRUE(10003, "商品信息不正确"),
    PRODUCT_STATUS_FAIL(10004, "修改商品状态失败"),



    ORDER_NOT_EXIST(20001, "订单不存在"),
    ORDER_DETAIL_NOT_EXIST(20002, "订单详情不存在"),
    ORDER_STATUS_ERROR(20003, "订单状态不正确"),
    ORDER_UPDATE_FAIL(20004, "订单状态更新失败"),
    ORDER_DETAIL_EMPTY(20005, "订单中无商品"),
    ORDER_PAY_STATUS_ERROR(20006, "订单支付状态不正确"),
    ORDER_CANCEL_FAIL(20007, "撤回失败，顺丰订单不能撤回"),
    //    ORDER_FINISH_SUCCESS(20008,"订单完结成功"),
    ORDER_OWNER_ERROR(20009, "该订单不属于当前用户"),
    CART_EMPTY(20010, "购物车不能为空"),
    SHOPPING_CART_COUNT_NOT_TRUE(20011, "购物车商品数量不正确"),
    BALANCE_NOT_ENOUGH(20012, "余额不足，支付失败"),
    AMOUNT_NOT_TRUE(20013, "金额不正确"),
    NOT_BALANCE_PAY(20014, "不能余额支付充值订单"),
    NOT_CARD_PAY(20015, "不能用卡，支付充值订单"),
    ORDER_NOT_SERVICE(20016,"该地区 暂不支持家居服务"),
    ORDER_BARCODE_EXIST(20017,"条形码已经存在"),


    WX_MP_ERROR(30001, "微信公众账号方面错误"),

    FILE_DELETION_FAILURE(30101, "文件删除失败"),
    FILE_UPLOAD_FAILURE(30102, "文件上传失败"),

    SF_DISPATCH_ORDER_FAILURE(30201, "快递下单失败"),
    SF_ROUTING_QUERY_FAILURE(30202, "快递订单路由查询失败"),

    XW_CARD_NOT_EXIST(30301, "卡不存在，请重新确认"),
    XW_CARD_PAY_FAIL(30305, "卡支付失败"),
    XW_CARD_STATUS_WRONG(30307, "卡状态错误，绑定失败,请核实卡是否有效"),
    XW_CARD_PHONE_WRONG(30302, "绑定失败，卡绑定的手机号和当前使用的手机号不同"),
    XW_CARD_TYPE_WRONG(30303, "绑定失败，（无折扣卡）卡类型不支持绑定"),
    XW_CARD_NOT_TRUE(30304, "绑定失败，卡号和卡绑定的手机号不匹配"),
    XW_CARD_REPEAT(30306, "绑定失败，绑定卡号和当前卡号相同"),


    ADDRESS_NOT_EXIST(30401, "地址不存在"),
    ADVERTISEMENT_NOT_EXIST(30402, "广告不存在"),
    STORE_INFO_NOT_TRUE(30403, "门店信息不正确"),
    STORE_NOT_EXIST(30404, "门店不存在"),
    GUIDE_CARD_NOT_EXIST(30405, "导卡详情不存在"),
    REFUND_NOT_EXIST(30406, "退款详情不存在"),
    PLATFORM_TEXT_NOT_EXIST(30407, "平台内容不正确"),
    AGENT_INFO_NOT_TRUE(30408, "代理商信息不正确"),
    AGENT_NOT_EXIST(30409, "代理商不存在"),
    AGENT_EXIST_REGION_DISTRIBUTION(30410,"区域设置错误，设置的区域已属于其他代理商"),
    CHARGEBACK_NOT_EXIST(30411,"扣款信息不存在"),
    CHARGEBACK_FAIL(30412,"余额不足，扣款失败"),


    USER_NOT_EXIST(40001, "用户不存在"),
    USER_INFO_NOT_TRUE(40002, "用户信息不正确"),
    CODE_NOT_TRUE(40003, "验证码不正确"),
    DO_NOT_PASS(40004, "验证不通过"),
    CODE_GAIN_FAILURE(40005, "验证码获取失败"),
    EXPIRED_VERIFICATION(40006, "验证过期"),
    ROLE_NOT_TRUE(40007, "用户名或密码错误"),
    PHONE_IS_REGISTER(40013,"手机已经被注册"),
    ROLE_IS_EXIST(40008, "用户已存在,请输入新的用户名"),
    ROLE_NOT_EXIST(40009, "账号不存在"),
    ROLE_UPDATE_FAIL(40010, "权限修改失败"),
    AUTHENTICATION_FAIL(40011, "权限不足"),
    PHONE_IS_EXIST(40012, "新旧手机号相同，绑定失败"),

    REQUEST_FAIL(42001, "请求失败，参数错误");


    private Integer code;

    private String message;

    ResultExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
