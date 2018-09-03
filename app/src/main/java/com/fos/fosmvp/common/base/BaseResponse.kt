package com.fos.fosmvp.common.base

import java.io.Serializable

/**
 * 封装服务器返回数据
 *
 */
class BaseResponse<T> : Serializable {
    var code = "-1"
    var msg: String? = null
    var data: T? = null

    /**
     * 判断返回值是否成功
     * @return
     */
    val isSucceed: Boolean
        get() = if (code == "0")
            true
        else
            false
}
