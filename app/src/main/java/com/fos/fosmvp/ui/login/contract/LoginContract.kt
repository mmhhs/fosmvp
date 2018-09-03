package com.fos.fosmvp.ui.login.contract


import com.fos.fosmvp.common.base.BaseModel
import com.fos.fosmvp.common.base.BasePresenter
import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.base.BaseView
import com.fos.fosmvp.entity.login.UserEntity

import io.reactivex.Observable


interface LoginContract {
    interface Model : BaseModel {
        //请求用户登录信息
        fun getLoginData(map: Map<String, String>): Observable<BaseResponse<UserEntity>>
    }

    interface View : BaseView {
        //登录成功返回
        fun returnLoginSucceed(userEntity: UserEntity)

        //登录失败返回
        fun returnLoginFail(baseResponse: BaseResponse<*>?, isVisitError: Boolean)
    }

    abstract class Presenter : BasePresenter<View, Model>() {
        //发起用户登录请求
        abstract fun getLoginRequest(tel: String, password: String)
    }
}
