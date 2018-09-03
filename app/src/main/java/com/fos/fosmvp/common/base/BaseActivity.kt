package com.fos.fosmvp.common.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fos.fosmvp.common.baserx.RxManager
import com.fos.fosmvp.common.utils.AppManager
import com.fos.fosmvp.common.utils.TUtil

/**
 * 基类Activity
 */
abstract class BaseActivity<T : BasePresenter<*, *>, E : BaseModel> : AppCompatActivity() {
    var mPresenter: T? = null
    var mModel: E? = null
    var mContext: Context? = null
    var mRxManager: RxManager? = null
    private var isConfigChange = false
    var appManager: AppManager? =null;


    /*********************
     * 子类实现
     */
    //获取布局文件
    abstract val layoutId: Int

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            isConfigChange = false
            mRxManager = RxManager()
            setContentView(layoutId)
            mContext = this
            mPresenter = TUtil.getT<T>(this, 0)
            mModel = TUtil.getT<E>(this, 1)
            if (mPresenter != null) {
                mPresenter!!.mContext = this
            }
            this.initPresenter()
            this.initView(savedInstanceState)
            appManager = AppManager.appManager
            if (appManager!=null)
                appManager!!.addActivity(this)
        } catch (e: Exception) {
        }
    }

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    abstract fun initPresenter()

    //初始化view
    abstract fun initView(savedInstanceState: Bundle?)


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isConfigChange = true
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (mPresenter != null) {
                mPresenter!!.onDestroy()
            }
            if (mRxManager != null) {
                mRxManager!!.clear()
            }
            try {
                if (!isConfigChange) {
                    appManager!!.finishActivity(this)
                }
            } catch (e: Exception) {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    public fun finishActivity(){
        try {
            appManager!!.finishActivity(this)
        } catch (e: Exception) {
        }
    }

}
