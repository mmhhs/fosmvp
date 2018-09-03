package com.fos.fosmvp.common.utils

import android.content.Context
import android.widget.Toast

import com.fos.fosmvp.common.base.BaseApplication


/**
 * Toast统一管理类
 */
object ToastUtils {
    private var toast: Toast? = null

    private fun initToast(message: CharSequence, duration: Int): Toast {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.appContext, message, duration)
        } else {
            toast!!.setText(message)
            toast!!.duration = duration
        }
        return toast!!
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    fun showShort(message: CharSequence) {
        initToast(message, Toast.LENGTH_SHORT).show()
    }


    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    fun showShort(strResId: Int) {
        //		Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
        initToast(BaseApplication.appContext!!.resources.getText(strResId), Toast.LENGTH_SHORT).show()
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    fun showLong(message: CharSequence) {
        initToast(message, Toast.LENGTH_LONG).show()
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    fun showLong(strResId: Int) {
        initToast(BaseApplication.appContext!!.resources.getText(strResId), Toast.LENGTH_LONG).show()
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    fun show(message: CharSequence, duration: Int) {
        initToast(message, duration).show()
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    fun show(context: Context, strResId: Int, duration: Int) {
        initToast(context.resources.getText(strResId), duration).show()
    }


}
