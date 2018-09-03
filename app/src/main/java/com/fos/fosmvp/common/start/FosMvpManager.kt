package com.fos.fosmvp.common.start

import com.fos.fosmvp.common.http.Api
import com.fos.fosmvp.common.utils.LogUtils

/**
 * 框架初始化
 */
object FosMvpManager {
    /** 提示文字：加载中  */
    var TASK_LOADING = "loading..."
    /** 提示文字：没有网络  */
    var TASK_NO_NETWORK = "no network"
    /** 提示文字：访问错误，服务器返回值无法解析  */
    var TASK_LINK_ERROR = "access error"

    /** 接口访问路径前缀  */
    const val PREFIX_URL = "https://bfda-app.ifoton.com.cn/est/"

    /** 是否调试  */
    var DEBUGGING = true

    fun init() {
        Api.initialize()
        LogUtils.setShowLogEnabled(DEBUGGING)


    }

}
