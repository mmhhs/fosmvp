package com.fos.fosmvp.common.baserx;

import android.content.Context;

import com.fos.fosmvp.common.base.BaseApplication;
import com.fos.fosmvp.common.start.FosMvpManager;
import com.fos.fosmvp.common.utils.NetWorkUtils;

import io.reactivex.observers.DisposableObserver;


/**
 * 订阅封装
 *
 */
public abstract class RxObserver<T> extends DisposableObserver<T> {
    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    public RxObserver(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxObserver(Context context) {
        this(context, FosMvpManager.TASK_LOADING, false);
    }

    public RxObserver(Context context, boolean showDialog) {
        this(context, FosMvpManager.TASK_LOADING, showDialog);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //TODO 开启加载框
    }

    @Override
    public void onComplete() {
        //TODO 关闭加载框
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //TODO 关闭加载框

        if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
            //没有网络
            _onError(FosMvpManager.TASK_NO_NETWORK);
        } else if (e instanceof ServerException) {
            //服务器
            _onError(e.getMessage());
        } else {
            //其它
            _onError(FosMvpManager.TASK_LINK_ERROR);
        }
    }

    /**
     * 访问成功处理
     * @param t
     */
    protected abstract void _onNext(T t);

    /**
     * 访问错误处理
     * @param message
     */
    protected abstract void _onError(String message);

    public boolean isShowDialog() {
        return showDialog;
    }

    /**
     * 是否显示浮动dialog
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
