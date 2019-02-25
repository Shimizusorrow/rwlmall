package com.lly835.bestpay.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author Curtain
 * @date 2018/9/25 14:33
 */

@XStreamAlias("xml")
@Data
public class QueryRequest {
    private String appid;

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    @XStreamAlias("notify_url")
    private String notifyUrl;

    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    private String sign;

}
