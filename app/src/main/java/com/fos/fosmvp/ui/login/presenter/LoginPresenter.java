package com.fos.fosmvp.ui.login.presenter;


import com.fos.fosmvp.base.BaseResponse;
import com.fos.fosmvp.baserx.RxSubscriber;
import com.fos.fosmvp.entity.login.UserEntity;
import com.fos.fosmvp.http.Api;
import com.fos.fosmvp.ui.login.contract.LoginContract;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;


public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void getLoginRequest(String tel, String password) {
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("name", tel);
        argMap.put("password", password);

        Observable<BaseResponse<UserEntity>> observable = Api.initObservable(mModel.getLoginResultData(argMap));
        mRxManage.add(observable.subscribe(new RxSubscriber<BaseResponse<UserEntity>>(mContext, true) {

            @Override
            protected void _onNext(BaseResponse<UserEntity> res) {
                if (res.isSucceed()) {
                    mView.returnLoginSucceed(res.getData());
                } else {
                    mView.returnLoginFail(res, false);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.returnLoginFail(null, true);
            }
        }));
    }


}
