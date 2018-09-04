package com.fos.fosmvp.common.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.fos.fosmvp.R

/**
 * 加载、空、错误视图
 * showStyle 类型：0：视图内加载；1：弹窗加载
 */
class LoadViewUtil(private val mContext: Context, private val contentView: View?
                   , private val message: String, private val showStyle: Int) {
    private var mParentView: View? = null//内容父视图

    private var mView: View? = null
    private var loadingView: LinearLayout? = null//加载视图
    private var txtLoadingTip: TextView? = null//加载提示
    private var layoutError: LinearLayout? = null//错误视图
    private var imgIcon: ImageView? = null//图标
    private var txtTip: TextView? = null//提示
    private var txtRetry: TextView? = null//重试
    private var txtOpt: TextView? = null//操作按钮

    private var mInFlater: LayoutInflater? = null
    private var mRootGroupView: ViewGroup? = null

    private var mState: Int = 0
    private var onOptionListener: OnOptionListener? = null
//    private val message = "loading..."//提示文字
//    private val showStyle = 0//类型：0：视图内加载；1：弹窗加载
    private var loadingDialog: LoadingDialog? = null

    init {
        init()
    }

    class Builder(private val context: Context) {
        private var message = "loading..."
        private var showStyle = 0

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setShowStyle(showStyle: Int): Builder {
            this.showStyle = showStyle
            return this
        }

        fun create(view: View): LoadViewUtil {
            return LoadViewUtil(context, view, message, showStyle)
        }
    }

    private fun init() {
        if (contentView == null) {
            throw NullPointerException("view is null.")
        }

        mInFlater = LayoutInflater.from(mContext)

        if (contentView !is ViewGroup) {
            mParentView = contentView.parent as View
        } else {
            mParentView = contentView
        }
        when (showStyle) {
            0 -> {
                if (mParentView == null) {
                    throw ExceptionInInitializerError("view's parent is null.")
                }

                if (mParentView !is ViewGroup) {
                    throw ExceptionInInitializerError("it's relative is parent and parent can't with view.")
                }

                initView()
                mRootGroupView = mParentView!!.parent as ViewGroup
                addViewInRoot(mView)
                setLoadText(message)
                showView(STATE_CONTENT)
            }
            1 -> {
                val loadBuilder = LoadingDialog.Builder(mContext)
                        .setMessage(message)
                loadingDialog = loadBuilder.create()
            }
        }

    }

    private fun initView() {
        if (mView == null) {
            mView = mInFlater!!.inflate(R.layout.item_load, null)
            loadingView = mView!!.findViewById<View>(R.id.layout_loading) as LinearLayout
            txtLoadingTip = mView!!.findViewById<View>(R.id.txt_tip) as TextView
            layoutError = mView!!.findViewById<View>(R.id.layout_error) as LinearLayout
            imgIcon = mView!!.findViewById<View>(R.id.img_icon) as ImageView
            txtTip = mView!!.findViewById<View>(R.id.txt_tip) as TextView
            txtRetry = mView!!.findViewById<View>(R.id.txt_retry) as TextView
            txtOpt = mView!!.findViewById<View>(R.id.txt_opt) as TextView
            layoutError!!.setOnClickListener {
                if (onOptionListener != null) {
                    onOptionListener!!.onRetry(layoutError)
                }
            }
            txtOpt!!.setOnClickListener {
                if (onOptionListener != null) {
                    onOptionListener!!.onOption(layoutError)
                }
            }

        }
    }

    private fun addViewInRoot(view: View?) {
        if (mRootGroupView == null) {
            return
        }
        if (view!!.parent == null) {
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            mRootGroupView!!.addView(view, params)
        }
    }


    /**
     * 显示错误
     * @param tip
     */
    fun showErrorView(tip: String) {
        setTip(tip)
        showView(STATE_ERROR)
    }

    /**
     * 显示加载
     */
    fun showLoadView() {
        when (showStyle) {
            0 -> showView(STATE_LOADING)
            1 -> loadingDialog!!.show()
        }
    }

    /**
     * 隐藏加载
     */
    fun hideLoadView() {
        when (showStyle) {
            0 -> showContentView()
            1 -> loadingDialog!!.hide()
        }
    }

    /**
     * 显示空数据
     * @param tip
     */
    fun showEmptyView(tip: String) {
        setTip(tip)
        showView(STATE_EMPTY)
    }

    /**
     * 显示正常
     */
    fun showContentView() {
        showView(STATE_CONTENT)
    }

    fun showView(state: Int) {
        if (mView == null) {
            return
        }
        mState = state
        mView!!.visibility = View.GONE
        mParentView!!.visibility = View.GONE
        loadingView!!.visibility = View.GONE
        layoutError!!.visibility = View.GONE
        when (mState) {
            STATE_ERROR -> {
                mView!!.visibility = View.VISIBLE
                layoutError!!.visibility = View.VISIBLE
                setIcon(R.mipmap.ic_error)
            }
            STATE_EMPTY -> {
                mView!!.visibility = View.VISIBLE
                layoutError!!.visibility = View.VISIBLE
                setIcon(R.mipmap.ic_empty)
            }
            STATE_LOADING -> {
                mView!!.visibility = View.VISIBLE
                loadingView!!.visibility = View.VISIBLE
            }
            STATE_CONTENT -> mParentView!!.visibility = View.VISIBLE
        }
    }

    fun setTip(tip: String) {
        txtTip!!.text = tip
    }

    fun setLoadText(tip: String) {
        txtLoadingTip!!.text = tip
    }

    fun setText(textView: TextView, tip: String) {
        textView.text = tip
    }

    fun setIcon(resourceId: Int) {
        imgIcon!!.setBackgroundResource(resourceId)
    }

    fun setViewsVisibility(view: View, visible: Boolean) {
        if (visible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    fun setOnOptionListener(onOptionListener: OnOptionListener) {
        this.onOptionListener = onOptionListener
    }

    interface OnOptionListener {
        fun onRetry(v: View?)
        fun onOption(v: View?)
    }

    companion object {
        val STATE_ERROR = 0x01//错误状态
        val STATE_EMPTY = 0x02//空状态
        val STATE_LOADING = 0x03//加载中状态
        val STATE_CONTENT = 0x00//显示状态
    }

}
