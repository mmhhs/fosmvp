package com.fos.fosmvp.baserx;


import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 *
 */
public class RxManager {
    public RxBus mRxBus = RxBus.getInstance();
    //管理rxbus订阅
    private Map<String, Observable<?>> mObservables = new HashMap<>();
    /*管理Observables 和 Subscribers订阅， 发送者*/
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    /**
     * RxBus注入监听
     * @param eventName
     * @param action1
     */
    public <T>void on(String eventName, Action1<T> action1) {
        Observable<T> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        /*订阅管理*/
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 单纯的Observables 和 Subscribers管理
     * @param m
     */
    public void add(Subscription m) {
        /*订阅管理*/
        mCompositeSubscription.add(m);
    }
    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    public void clear() {
        mCompositeSubscription.clear();// 取消所有订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除rxbus观察
        }
    }
    //发送rxbus
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

//    public void ob(){
//        //观察者模式,这里产生事件,事件产生后发送给接受者,但是一定要记得将事件的产生者和接收者捆绑在一起,否则会出现错误
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                //这里调用的方法会在产生事件之后会发送给接收者,接收者对应方法会收到
//                e.onNext("hahaha");
//                e.onError(new Exception("wulala"));
//                e.onComplete();
//            }}).subscribe(new Observer<String>() {
//            //接受者,根据事件产生者产生的事件调用不同方法
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.e(TAG, "onSubscribe: ");
//            }
//            @Override
//            public void onNext(String value) {
//                Log.e(TAG, "onNext: " + value);
//            }
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, "onError: ", e);
//            }
//            @Override
//            public void onComplete() {
//                Log.e(TAG, "onComplete: ");
//            }
//        });
//    }
}
