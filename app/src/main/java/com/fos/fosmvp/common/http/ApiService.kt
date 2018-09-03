package com.fos.fosmvp.common.http

import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.start.FosMvpManager
import com.fos.fosmvp.entity.login.UserEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {
    //登录
    @FormUrlEncoded
    @POST("getLoginMember.action")
    fun login(@FieldMap map: Map<String, String>): Observable<BaseResponse<UserEntity>>

    //车联网接口 -实时车况
    @GET(FosMvpManager.PREFIX_URL + "/openapi/iov/business/canBus/getByVinAndCode.json")
    fun getRealTimeData(@Query("vin") vin: String, @Query("token") token: String): Observable<BaseResponse<*>>

    //提交开通车联网
    @Multipart
    @Headers("lock:yes")
    @POST("insertBindingCar.action")
    fun insertBindingCar(@PartMap map: Map<String, RequestBody>, @Part parts: List<MultipartBody.Part>): Observable<BaseResponse<*>>

}
