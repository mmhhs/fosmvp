package com.fos.fosmvp.ui.login.presenter


import com.fos.fosmvp.common.base.BaseApplication
import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.baserx.RxObserver
import com.fos.fosmvp.common.http.Api
import com.fos.fosmvp.entity.login.UserEntity
import com.fos.fosmvp.ui.login.contract.LoginContract
import io.reactivex.Observer



class LoginPresenter : LoginContract.Presenter() {
    override fun getLoginRequest(tel: String, password: String) {
        var rxObserver: Observer<BaseResponse<UserEntity>>? = null
        val argMap = HashMap<String, String>()
        argMap.put("name", tel)
        argMap.put("password", password)
        val observable = Api.initObservable(mModel!!.getLoginData(argMap))
        rxObserver = object : RxObserver<BaseResponse<UserEntity>>(BaseApplication.appContext!!) {
            override fun _onNext(res: BaseResponse<UserEntity>) {
                if (res.isSucceed) {
                    mView!!.returnLoginSucceed(res.data!!)
                } else {
                    mView!!.returnLoginFail(res, false)
                }
            }

            override fun _onError(message: String) {
                mView!!.returnLoginFail(null, true)
            }
        }
//        observable.subscribe(rxObserver)
        mRxManage.add(rxObserver)
    }


}
