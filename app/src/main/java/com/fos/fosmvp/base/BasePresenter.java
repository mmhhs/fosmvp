package com.fos.fosmvp.base;

import android.content.Context;

import com.fos.fosmvp.baserx.RxManager;


/**
 * 基类presenter
 */
public abstract class BasePresenter<T,E>{
    public Context mContext;
    public T mView;
    public E mModel;
    public RxManager mRxManage = new RxManager();

    public void setViewModel(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }
    public void onStart(){

    }
    public void onDestroy() {
        mRxManage.clear();
    }
}
