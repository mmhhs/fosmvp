package com.fos.fosmvp.ui.login.presenter;


import com.fos.fosmvp.common.base.BaseApplication;
import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.baserx.RxObserver;
import com.fos.fosmvp.entity.login.UserEntity;
import com.fos.fosmvp.common.http.Api;
import com.fos.fosmvp.ui.login.contract.LoginContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void getLoginRequest(String tel, String password) {
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("name", tel);
        argMap.put("password", password);
        Observable<BaseResponse<UserEntity>> observable = Api.initObservable(getMModel().getLoginData(argMap));
        RxObserver rxObserver = new RxObserver<BaseResponse<UserEntity>>(BaseApplication.Companion.getAppContext()) {
            @Override
            protected void _onNext(BaseResponse<UserEntity> res) {
                if (res.isSucceed()) {
                    getMView().returnLoginSucceed(res.getData());
                } else {
                    getMView().returnLoginFail(res, false);
                }
            }

            @Override
            protected void _onError(String message) {
                getMView().returnLoginFail(null, true);
            }
        };
        observable.subscribe(rxObserver);
        getMRxManage().add(rxObserver);
    }


}
