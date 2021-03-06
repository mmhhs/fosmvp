package com.fos.fosmvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fos.fosmvp.baserx.RxManager;
import com.fos.fosmvp.utils.TUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类fragment
 *
 */
public abstract  class BaseFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {
    protected View rootView;
    public T mPresenter;
    public E mModel;
    public RxManager mRxManager;
    public Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
        {
            rootView = inflater.inflate(getLayoutResource(), container, false);
            mRxManager=new RxManager();
            mPresenter = TUtil.getT(this, 0);
            mModel= TUtil.getT(this,1);
            if(mPresenter!=null){
                mPresenter.mContext=this.getActivity();
            }
            initPresenter();
            initView(savedInstanceState);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        try {
            mUnbinder = ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //获取布局文件
    protected abstract int getLayoutResource();
    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();
    //初始化view
    protected abstract void initView(Bundle savedInstanceState);

    public void initPresenter2(){
        mPresenter.setViewModel(this,mModel);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mUnbinder.unbind();//释放所有绑定的view
            if (mPresenter != null)
                mPresenter.onDestroy();
            mRxManager.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
