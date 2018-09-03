package com.fos.fosmvp.common.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import com.fos.fosmvp.R

/**
 * 加载框
 * 使用示例：
 * LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
 * .setMessage("加载中...")
 * .setCancelable(true)
 * .setCancelOutside(true);
 * LoadingDailog dialog=loadBuilder.create();
 * dialog.show();
 */
class LoadingDialog : Dialog {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    class Builder(private val context: Context) {
        private var message = "loading..."
        private var isShowMessage = true
        private var isCancelable = false
        private var isCancelOutside = false

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        fun setShowMessage(isShowMessage: Boolean): Builder {
            this.isShowMessage = isShowMessage
            return this
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        fun setCancelOutside(isCancelOutside: Boolean): Builder {
            this.isCancelOutside = isCancelOutside
            return this
        }

        fun create(): LoadingDialog {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_loading_dialog, null)
            val loadingDailog = LoadingDialog(context, R.style.LoadingDialogStyle)
            val msgText = view.findViewById<View>(R.id.txt_loading_tip) as TextView
            if (isShowMessage) {
                msgText.text = message
            } else {
                msgText.visibility = View.GONE
            }
            loadingDailog.setContentView(view)
            loadingDailog.setCancelable(isCancelable)
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside)
            return loadingDailog

        }


    }
}
