package com.fos.fosmvp.common.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fos.fosmvp.common.baserx.RxManager;
import com.fos.fosmvp.common.utils.AppManager;
import com.fos.fosmvp.common.utils.TUtil;


/**
 * 基类Activity
 */
public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {
    public T mPresenter;
    public E mModel;
    public Context mContext;
    public RxManager mRxManager;
    private boolean isConfigChange = false;
//    public Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isConfigChange = false;
        mRxManager = new RxManager();
        setContentView(getLayoutId());
//        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        this.initPresenter();
        this.initView(savedInstanceState);
    }


    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    public abstract void initView(Bundle savedInstanceState);

    public void setPresenter(){
        mPresenter.setViewModel(this,mModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigChange = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mPresenter != null){
                mPresenter.onDestroy();
            }
            if (mRxManager != null) {
                mRxManager.clear();
            }
            if (!isConfigChange) {
                AppManager.getAppManager().finishActivity(this);
            }
//            mUnbinder.unbind();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
