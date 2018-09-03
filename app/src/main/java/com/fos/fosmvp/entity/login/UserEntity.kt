package com.fos.fosmvp.entity.login


import java.io.Serializable

/**
 * 用户信息实体
 */
class UserEntity(var uid: String//用户主键
                 , var memberID: String//会员id
) : Serializable {
    var test: String? = null
}
