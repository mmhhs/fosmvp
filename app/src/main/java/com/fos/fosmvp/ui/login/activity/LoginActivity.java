package com.fos.fosmvp.ui.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fos.fosmvp.R;
import com.fos.fosmvp.common.base.BaseActivity;
import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.entity.login.UserEntity;
import com.fos.fosmvp.ui.login.contract.LoginContract;
import com.fos.fosmvp.ui.login.model.LoginModel;
import com.fos.fosmvp.ui.login.presenter.LoginPresenter;
import com.fos.fosmvp.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity使用示例
 */
public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {
    @BindView(R.id.edt_login_phone)
    EditText loginPhoneEdt;
    @BindView(R.id.edt_login_password)
    EditText loginPassEdt;
    public String tel = "";
    public String password = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        getMPresenter().setViewModel(this, getMModel());
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }



    @OnClick({R.id.btn_user_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_user_login:
                login();
                break;
        }
    }


    public void login() {
        tel = loginPhoneEdt.getText().toString();
        password = loginPassEdt.getText().toString();
        getMPresenter().getLoginRequest(tel,password);
    }

    @Override
    public void returnLoginSucceed(UserEntity userEntity) {
        ToastUtils.showShort("登录成功");
        finish();
    }


    @Override
    public void returnLoginFail(BaseResponse baseResponse,boolean isVisitError) {
        try {
            if (!isVisitError){
                ToastUtils.showShort(baseResponse.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
