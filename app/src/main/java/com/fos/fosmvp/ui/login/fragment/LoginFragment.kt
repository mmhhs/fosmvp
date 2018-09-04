package com.fos.fosmvp.ui.login.fragment

import android.os.Bundle
import android.view.View
import com.fos.fosmvp.R
import com.fos.fosmvp.common.base.BaseFragment
import com.fos.fosmvp.common.base.BaseResponse
import com.fos.fosmvp.common.utils.LogUtils
import com.fos.fosmvp.common.utils.ToastUtils
import com.fos.fosmvp.entity.login.UserEntity
import com.fos.fosmvp.ui.login.contract.LoginContract
import com.fos.fosmvp.ui.login.model.LoginModel
import com.fos.fosmvp.ui.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Fragment使用示例
 *
 */
class LoginFragment : BaseFragment<LoginPresenter, LoginModel>(), LoginContract.View,View.OnClickListener {
    var tel = ""
    var password = ""

    override val layoutResource: Int
        get() = R.layout.activity_login

    override fun initPresenter() {
        mPresenter!!.setViewModel(this, mModel!!)
    }

    override fun initView(savedInstanceState: Bundle?) {
        btn_user_login.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.btn_user_login -> login()
        }
    }

    fun login() {
        tel = edt_login_phone.text.toString().trim()
        password = edt_login_password.text.toString().trim()
        LogUtils.e("tel= "+tel)
        ToastUtils.showShort("登录")
        mPresenter!!.getLoginRequest(tel, password)
    }

    override fun returnLoginSucceed(userEntity: UserEntity) {
        ToastUtils.showShort("登录成功")
    }


    override fun returnLoginFail(baseResponse: BaseResponse<*>?, isVisitError: Boolean) {
        try {
            if (!isVisitError&&baseResponse!=null) {
                ToastUtils.showShort(baseResponse.msg!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
