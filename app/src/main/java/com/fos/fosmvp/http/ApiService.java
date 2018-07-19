package com.fos.fosmvp.http;

import com.fos.fosmvp.base.BaseResponse;
import com.fos.fosmvp.start.FosMvpManager;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiService {
    //登录
    @FormUrlEncoded
    @POST("getLogin.action")
    Observable<BaseResponse> login(@FieldMap Map<String, String> map);

    //车联网接口 -实时车况
    @GET(FosMvpManager.PREFIX_URL + "/openapi/iov/business/canBus/getByVinAndCode.json")
    Observable<BaseResponse> getRealTimeData(@Query("vin") String vin, @Query("token") String token);

}
