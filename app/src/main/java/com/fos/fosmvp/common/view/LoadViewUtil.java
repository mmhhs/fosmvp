package com.fos.fosmvp.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fos.fosmvp.R;
import com.fos.fosmvp.start.FosMvpManager;

/**
 * 加载、空、错误视图
 * showStyle 类型：0：视图内加载；1：弹窗加载
 */
public class LoadViewUtil {
    public static final int STATE_ERROR = 0x01;//错误状态
    public static final int STATE_EMPTY = 0x02;//空状态
    public static final int STATE_LOADING = 0x03;//加载中状态
    public static final int STATE_CONTENT = 0x00;//显示状态

    private Context mContext;
    private View contentView;//内容视图
    private View mParentView;//内容父视图

    private View mView;
    private LinearLayout layoutLoading;//加载视图
    private LinearLayout layoutError;//错误视图
    private ImageView imgIcon;//图标
    private TextView txtTip;//提示
    private TextView txtRetry;//重试
    private TextView txtOpt;//操作按钮
    private TextView txtLoadingTip;//加载提示

    private LayoutInflater mInFlater;
    private ViewGroup mRootGroupView;

    private int mState;
    private OnOptionListener onOptionListener;
    private String message = "loading...";//提示文字

    private int showStyle = 0;//类型：0：视图内加载；1：弹窗加载
    private LoadingDialog loadingDialog;

    public LoadViewUtil(Context mContext, View contentView,String message,int showStyle) {
        this.mContext = mContext;
        this.contentView = contentView;
        this.message = message;
        this.showStyle = showStyle;
        init();
    }

    public static class Builder {
        private Context context;
        private String message = "loading...";
        private int showStyle = 0;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setShowStyle(int showStyle) {
            this.showStyle = showStyle;
            return this;
        }

        public LoadViewUtil create(View view) {
            LoadViewUtil loadViewUtil = new LoadViewUtil(context,view,message,showStyle);
            return loadViewUtil;
        }
    }

    private void init() {
        if (contentView == null) {
            throw new NullPointerException("view is null.");
        }

        mInFlater = LayoutInflater.from(mContext);

        if (!(contentView instanceof ViewGroup)) {
            mParentView = (View) contentView.getParent();
        } else {
            mParentView = contentView;
        }
        switch (showStyle) {
            case 0:
                if (mParentView == null) {
                    throw new ExceptionInInitializerError("view's parent is null.");
                }

                if (!(mParentView instanceof ViewGroup)) {
                    throw new ExceptionInInitializerError("it's relative is parent and parent can't with view.");
                }

                initView();
                mRootGroupView = (ViewGroup) mParentView.getParent();
                addViewInRoot(mView);
                setLoadText(message);
                showView(STATE_CONTENT);
                break;
            case 1:
                LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(mContext)
                        .setMessage(message);
                loadingDialog = loadBuilder.create();
                break;
        }

    }

    private void initView() {
        if (mView == null) {
            mView = mInFlater.inflate(R.layout.item_load, null);
            layoutLoading = (LinearLayout) mView.findViewById(R.id.layout_loading);
            layoutError = (LinearLayout) mView.findViewById(R.id.layout_error);
            imgIcon = (ImageView) mView.findViewById(R.id.img_icon);
            txtTip = (TextView) mView.findViewById(R.id.txt_tip);
            txtRetry = (TextView) mView.findViewById(R.id.txt_retry);
            txtOpt = (TextView) mView.findViewById(R.id.txt_opt);
            txtLoadingTip = (TextView) mView.findViewById(R.id.txt_loading_tip);
            layoutError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOptionListener != null) {
                        onOptionListener.onRetry(layoutError);
                    }
                }
            });
            txtOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOptionListener != null) {
                        onOptionListener.onOption(layoutError);
                    }
                }
            });

        }
    }

    private void addViewInRoot(View view) {
        if (mRootGroupView == null) {
            return;
        }
        if (view.getParent() == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mRootGroupView.addView(view, params);
        }
    }


    /**
     * 显示错误
     * @param tip
     */
    public void showErrorView(String tip) {
        setTip(tip);
        showView(STATE_ERROR);
    }

    /**
     * 显示加载
     */
    public void showLoadView() {
        switch (showStyle){
            case 0:
                showView(STATE_LOADING);
                break;
            case 1:
                loadingDialog.show();
                break;
        }
    }

    /**
     * 隐藏加载
     */
    public void hideLoadView() {
        switch (showStyle){
            case 0:
                showContentView();
                break;
            case 1:
                loadingDialog.hide();
                break;
        }
    }

    /**
     * 显示空数据
     * @param tip
     */
    public void showEmptyView(String tip) {
        setTip(tip);
        showView(STATE_EMPTY);
    }

    /**
     * 显示正常
     */
    public void showContentView() {
        showView(STATE_CONTENT);
    }

    public void showView(int state) {
        if (mView == null) {
            return;
        }
        mState = state;
        mView.setVisibility(View.GONE);
        mParentView.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        switch (mState) {
            case STATE_ERROR:
                mView.setVisibility(View.VISIBLE);
                layoutError.setVisibility(View.VISIBLE);
                setIcon(R.mipmap.ic_error);
                break;
            case STATE_EMPTY:
                mView.setVisibility(View.VISIBLE);
                layoutError.setVisibility(View.VISIBLE);
                setIcon(R.mipmap.ic_empty);
                break;
            case STATE_LOADING:
                mView.setVisibility(View.VISIBLE);
                layoutLoading.setVisibility(View.VISIBLE);
                txtLoadingTip.setText(FosMvpManager.TASK_LOADING);
                break;
            case STATE_CONTENT:
                mParentView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setTip(String tip) {
        txtTip.setText(tip);
    }

    public void setLoadText(String tip) {
        txtLoadingTip.setText(tip);
    }

    public void setText(TextView textView, String tip) {
        textView.setText(tip);
    }

    public void setIcon(int resourceId) {
        imgIcon.setBackgroundResource(resourceId);
    }

    public void setViewsVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setOnOptionListener(OnOptionListener onOptionListener) {
        this.onOptionListener = onOptionListener;
    }

    public interface OnOptionListener {
        void onRetry(View v);
        void onOption(View v);
    }

}
