package com.fos.fosmvp.common.utils

import android.util.Log

/**
 * 日志打印类
 */
object LogUtils {
    private var isDebug = false
    private val TAG = "LogUtils"

    fun out(msg: String) {
        if (isDebug)
            println(msg)
    }

    fun d(msg: String) {
        if (isDebug)
            Log.d(TAG, msg)
    }

    fun i(msg: String) {
        if (isDebug)
            Log.i(TAG, msg)
    }

    fun v(msg: String) {
        if (isDebug)
            Log.v(TAG, msg)
    }

    fun w(msg: String) {
        if (isDebug)
            Log.w(TAG, msg)
    }

    fun e(msg: String) {
        if (isDebug)
            Log.e(TAG, msg)
    }

    fun setShowLogEnabled(showLogEnabled: Boolean) {
        LogUtils.isDebug = showLogEnabled
    }

}
