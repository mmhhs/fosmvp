package com.fos.fosmvp.common.baserx

import android.content.Context
import android.view.View

import com.fos.fosmvp.common.base.BaseApplication
import com.fos.fosmvp.common.start.FosMvpManager
import com.fos.fosmvp.common.utils.NetWorkUtils
import com.fos.fosmvp.common.view.LoadViewUtil

import io.reactivex.observers.DisposableObserver


/**
 * 订阅封装
 *
 */
abstract class RxObserver<T> @JvmOverloads constructor(private val mContext: Context, showDialog: Boolean = false) : DisposableObserver<T>() {
    /**
     * 是否显示浮动dialog
     */
    var isShowDialog = true//是否显示加载框
    private var showStyle = 1//加载框类型：0：视图内加载；1：弹窗加载
    private var message: String? = null//加载提示文字
    private var contentView: View? = null//内容视图
    private var loadViewUtil: LoadViewUtil? = null

    init {
        this.isShowDialog = showDialog
    }

    override fun onStart() {
        super.onStart()
        //TODO 开启加载框
        if (isShowDialog) {
            val builder = LoadViewUtil.Builder(mContext)
                    .setMessage(message!!)
                    .setShowStyle(showStyle)
            loadViewUtil = builder.create(contentView!!)
            loadViewUtil!!.showLoadView()
        }
    }

    override fun onComplete() {
        //TODO 关闭加载框
        if (isShowDialog) {
            loadViewUtil!!.hideLoadView()
        }
    }

    override fun onNext(t: T) {
        _onNext(t)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()

        var errorMsg = ""
        if (!NetWorkUtils.isNetConnected(BaseApplication.appContext!!)) {
            //没有网络
            _onError(FosMvpManager.TASK_NO_NETWORK)
            errorMsg = FosMvpManager.TASK_NO_NETWORK
        } else if (e is ServerException) {
            //服务器
            _onError(""+e.message)
            errorMsg = ""+e.message
        } else {
            //其它
            _onError(FosMvpManager.TASK_LINK_ERROR)
            errorMsg = FosMvpManager.TASK_LINK_ERROR
        }

        //TODO 关闭加载框
        if (isShowDialog && showStyle == 0) {
            loadViewUtil!!.showErrorView(errorMsg)
        }
    }

    /**
     * 访问成功处理
     * @param t
     */
    protected abstract fun _onNext(t: T)

    /**
     * 访问错误处理
     * @param message
     */
    protected abstract fun _onError(message: String)

    /**
     * 设置加载框默认值
     * @param showStyle 加载框类型：0：视图内加载；1：弹窗加载
     * @param message 加载提示文字
     * @param contentView 内容视图 视图内加载时，必须要传
     */
    fun setDialogStyle(showStyle: Int, message: String, contentView: View) {
        this.showStyle = showStyle
        this.message = message
        this.contentView = contentView
    }

}
