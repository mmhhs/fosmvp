package com.fos.fosmvp.common.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fos.fosmvp.common.baserx.RxManager
import com.fos.fosmvp.common.utils.TUtil

/**
 * 基类fragment
 *
 */
abstract class BaseFragment<T : BasePresenter<*, *>, E : BaseModel> : Fragment() {
    var rootView: View? = null
    var mPresenter: T? = null
    var mModel: E? = null
    var mRxManager: RxManager? = null


    //获取布局文件
    protected abstract val layoutResource: Int

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater!!.inflate(layoutResource, container, false)
            mRxManager = RxManager()
            mPresenter = TUtil.getT<T>(this, 0)
            mModel = TUtil.getT<E>(this, 1)
            if (mPresenter != null) {
                mPresenter!!.mContext = this.activity
            }
            initPresenter()
            initView(savedInstanceState)
        }
        val parent = rootView!!.parent as ViewGroup
        parent?.removeView(rootView)
        return rootView
    }


    override fun onResume() {
        super.onResume()
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    abstract fun initPresenter()

    //初始化view
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 通过Class跳转界面
     */
    fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        startActivityForResult(cls, null, requestCode)
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    fun startActivityForResult(cls: Class<*>, bundle: Bundle?,
                               requestCode: Int) {
        val intent = Intent()
        intent.setClass(activity, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    @JvmOverloads
    fun startActivity(cls: Class<*>, bundle: Bundle? = null) {
        val intent = Intent()
        intent.setClass(activity, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            if (mPresenter != null)
                mPresenter!!.onDestroy()
            if (mRxManager != null)
                mRxManager!!.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
/**
 * 通过Class跳转界面
 */
