package com.fos.fosmvp.entity

import java.io.Serializable

/**
 * 接口返回值实体父类
 */
class ResultEntity : Serializable {

    var code = "-1"
    var msg: String? = null
}
