package com.lly835.bestpay.model.wxpay;

import com.lly835.bestpay.model.wxpay.response.WxPayRefundResponse;
import com.lly835.bestpay.model.wxpay.response.WxPaySyncResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Curtain
 * @date 2018/9/25 14:45
 */
public interface WxPayApi {
    /**
     * 统一下单
     * @param body
     * @return
     */
    @POST("/pay/unifiedorder")
    Call<WxPaySyncResponse> unifiedorder(@Body RequestBody body);

    /**
     * 申请退款
     * @param body
     * @return
     */
    @POST("/secapi/pay/refund")
    Call<WxPayRefundResponse> refund(@Body RequestBody body);

//    @POST("pay/orderquery")
//    Call<Object> query(@Body RequestBody body);
}
