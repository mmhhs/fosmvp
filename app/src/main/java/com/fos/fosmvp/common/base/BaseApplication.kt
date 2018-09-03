package com.fos.fosmvp.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

import com.fos.fosmvp.common.start.FosMvpManager
import com.fos.fosmvp.common.utils.LogUtils

/**
 * Created by mmh on 2018/7/17.
 */
class BaseApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        FosMvpManager.init()
        LogUtils.e("---------------start fosmvp----------------" + baseApplication!!)
    }

    override fun onTerminate() {
        super.onTerminate()
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    companion object {
        private var baseApplication: BaseApplication? = null

        val appContext: Context?
            get() = baseApplication
    }
}
