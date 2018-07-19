package com.fos.fosmvp.baserx;

import android.content.Context;

import com.fos.fosmvp.base.BaseApplication;
import com.fos.fosmvp.start.FosMvpManager;
import com.fos.fosmvp.utils.NetWorkUtils;

import rx.Subscriber;

/**
 * 订阅封装
 *
 */
public abstract class RxSubscriber<T> extends Subscriber<T> {
    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxSubscriber(Context context) {
        this(context, FosMvpManager.TASK_LOADING, false);
    }

    public RxSubscriber(Context context, boolean showDialog) {
        this(context, FosMvpManager.TASK_LOADING, showDialog);
    }

    @Override
    public void onCompleted() {
        //TODO 关闭加载框

    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO 判断是否显示加载框

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
