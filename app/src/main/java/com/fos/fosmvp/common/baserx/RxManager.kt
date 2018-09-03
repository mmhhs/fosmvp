package com.fos.fosmvp.common.baserx


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

/**
 * 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 *
 */
class RxManager {
    /*管理Observables 和 DisposableObserver订阅， 发送者*/
    private val mDisposables = CompositeDisposable()

    /**
     * 单纯的Observables 和 DisposableObserver管理
     * @param d
     */
    fun add(d: DisposableObserver<*>) {
        /*订阅管理*/
        mDisposables.add(d)
    }

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    fun clear() {
        mDisposables.clear()// 取消所有订阅
    }

}
