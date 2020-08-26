package com.baobaoloufu.android.yunpay.api;

import com.baobaoloufu.android.yunpay.bean.PriceBean;
import com.baobaoloufu.android.yunpay.bean.SingleConsumerBean;
import com.baobaoloufu.android.yunpay.constant.ApiConstants;
import com.baobaoloufu.android.yunpay.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    /**
     *获取聊天状态
     */
    @GET("sapi/hcp/consumers/status/single")
    Observable<BaseResponse<SingleConsumerBean>> getChatStatus(@Header("x-token") String header,@Query("hcpId") String hcpId);


    /**
     *结束聊天
     */
    @GET("sapi/rongyun/chatEnd")
    Observable<BaseResponse<Object>> endChat(@Header("x-token") String header,@Query("to") String targetId,@Query("present_type") int type);

    /**
     *发送自定义消息
     */
    @POST("sapi/rongyun/msg")
    Observable<BaseResponse<Object>> doctorSendMessage(@Body RequestBody body);

    /**
     *患者端发送消息调用 为了更新consumer_status 记录是否患者给医生发消息
     */
    @GET("sapi/hcp/addConsumerStatus")
    Observable<BaseResponse<Object>> addConsumerStatus(@Header("x-token") String header,@Query("hcp") String hcpId);
}
