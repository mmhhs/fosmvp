package com.fos.fosmvp.common.baserx;

import android.content.Context;
import android.view.View;

import com.fos.fosmvp.common.base.BaseApplication;
import com.fos.fosmvp.start.FosMvpManager;
import com.fos.fosmvp.common.utils.NetWorkUtils;
import com.fos.fosmvp.common.view.LoadViewUtil;

import io.reactivex.observers.DisposableObserver;


/**
 * 订阅封装
 *
 */
public abstract class RxObserver<T> extends DisposableObserver<T> {
    private Context mContext;
    private boolean showDialog = false;//是否显示加载框
    private int showStyle = 1;//加载框类型：0：视图内加载；1：弹窗加载
    private String message;//加载提示文字
    private View contentView;//内容视图
    private LoadViewUtil loadViewUtil;

    public RxObserver(Context context, boolean showDialog) {
        this.mContext = context;
        this.showDialog = showDialog;
    }

    public RxObserver(Context context) {
        this(context, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO 开启加载框
        if (showDialog){
            LoadViewUtil.Builder builder = new LoadViewUtil.Builder(mContext)
                    .setMessage(message)
                    .setShowStyle(showStyle);
            loadViewUtil = builder.create(contentView);
            loadViewUtil.showLoadView();
        }
    }

    @Override
    public void onComplete() {
        //TODO 关闭加载框
        if (showDialog){
            loadViewUtil.hideLoadView();
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        String errorMsg = "";
        if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
            //没有网络
            _onError(FosMvpManager.TASK_NO_NETWORK);
            errorMsg = FosMvpManager.TASK_NO_NETWORK;
        } else if (e instanceof ServerException) {
            //服务器
            _onError(e.getMessage());
            errorMsg = e.getMessage();
        } else if (e instanceof Exception) {
            //服务器
            _onError(e.getMessage());
            errorMsg = e.getMessage();
        } else {
            //其它
            _onError(FosMvpManager.TASK_LINK_ERROR);
            errorMsg = FosMvpManager.TASK_LINK_ERROR;
        }

        //TODO 关闭加载框
        if (showDialog){
            if (showStyle==0){
                loadViewUtil.showErrorView(errorMsg);
            } else {
                loadViewUtil.hideLoadView();
            }
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

    /**
     * 设置加载框默认值
     * @param showStyle 加载框类型：0：视图内加载；1：弹窗加载
     * @param message 加载提示文字
     * @param contentView 内容视图 视图内加载时，必须要传
     */
    public void setDialogStyle(int showStyle,String message,View contentView) {
        this.showStyle = showStyle;
        this.message = message;
        this.contentView = contentView;
    }

}
