package com.fos.fosmvp.http;

import com.fos.fosmvp.base.BaseResponse;
import com.fos.fosmvp.entity.login.UserEntity;
import com.fos.fosmvp.start.FosMvpManager;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiService {
    //登录
    @FormUrlEncoded
    @POST("getLogin.action")
    Observable<BaseResponse<UserEntity>> login(@FieldMap Map<String, String> map);

    //车联网接口 -实时车况
    @GET(FosMvpManager.PREFIX_URL + "/openapi/iov/business/canBus/getByVinAndCode.json")
    Observable<BaseResponse> getRealTimeData(@Query("vin") String vin, @Query("token") String token);

    //提交开通车联网
    @Multipart
    @Headers("lock:yes")
    @POST("insertBindingCar.action")
    Observable<BaseResponse> insertBindingCar(@PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> parts);

}
