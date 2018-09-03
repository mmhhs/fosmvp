package com.fos.fosmvp.common.base

import android.content.Context

import com.fos.fosmvp.common.baserx.RxManager


/**
 * 基类presenter
 */
abstract class BasePresenter<T, E> {
    var mContext: Context? = null
    var mView: T? = null
    var mModel: E? = null
    var mRxManage = RxManager()

    fun setViewModel(v: T, m: E) {
        this.mView = v
        this.mModel = m
        this.onStart()
    }

    fun onStart() {

    }

    fun onDestroy() {
        mRxManage.clear()
    }
}
