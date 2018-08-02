package com.fos.fosmvp.ui.login.model;


import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.entity.login.UserEntity;
import com.fos.fosmvp.common.http.Api;
import com.fos.fosmvp.common.http.ApiService;
import com.fos.fosmvp.ui.login.contract.LoginContract;

import java.util.Map;

import io.reactivex.Observable;


public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<BaseResponse<UserEntity>> getLoginData(Map map) {
        return Api.createApi(ApiService.class).login(map);
    }



}
