package com.fos.fosmvp.ui.login.model


import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.http.Api
import com.fos.fosmvp.common.http.ApiService
import com.fos.fosmvp.entity.login.UserEntity
import com.fos.fosmvp.ui.login.contract.LoginContract
import io.reactivex.Observable


class LoginModel : LoginContract.Model {
    override fun getLoginData(map: Map<String, String>): Observable<BaseResponse<UserEntity>> {
        return Api.createApi(ApiService::class.java).login(map)
    }


}
