package com.qunchuang.rwlmall.enums;

import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/5/15 13:51
 */

@Getter
public enum  RoleAuthorityFunctionEnum {

    /*订单洗衣*/
    ORDER_LAUNDRY("A1"),
    /*订单高端洗护*/
    ORDER_HIGH_LAUNDRY("A2"),
    /*订单家居*/
    ORDER_FURNITURE("A3"),
    /*订单商城*/
    ORDER_MALL("A4"),
    /*订单分析*/
    ORDER_ANALYSE("A5"),
    /*用户统计*/
    USER_STATISTICAL("B1"),
    /*消费统计*/
    CONSUME_STATISTICAL("B2"),
    /*反馈*/
    FEEDBACK("B3"),
    /*广告设置*/
    ADVERTISEMENT("C1"),
    /*洗衣设置*/
    PRODUCT_LAUNDRY("C2"),
    /*高端洗护设置*/
    PRODUCT_HIGH_LAUNDRY("C3"),
    /*家具设置*/
    PRODUCT_FURNITURE("C4"),
    /*商城设置*/
    PRODUCT_MALL("C5"),
    /*财务管理*/
    FINANCIAL_MANAGE("D1"),
    /*商户管理*/
    STORE_STATISTICAL("D2"),
    /*财务收支明细*/
    FINANCIAL_STATISTICAL("D3"),
    /*财务扣款*/
    FINANCIAL_CHARGE_BACK("D4"),
    /*充值设置*/
    RECHARGE("E1"),
    /*提成设置*/
    COMMISSION("E2"),
    /*账号管理*/
    ACCOUNT_MANGER("E3"),
    /*平台编辑*/
    PLATFORM_TEXT("E4"),
    /*运费设置*/
    FREIGHT_SET("E5"),

    /*代理商账号管理*/
    AGENT_MANGER("E6")
    ;

//    private String message;
    private String code;

    RoleAuthorityFunctionEnum(String code){
        this.code = code;
//        this.message = message;
    }
}
