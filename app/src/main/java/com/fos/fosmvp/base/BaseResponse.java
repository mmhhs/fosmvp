package com.fos.fosmvp.base;

import java.io.Serializable;

/**
 * 封装服务器返回数据
 *
 */
public class BaseResponse<T>  implements Serializable {
    public String code="-1";
    public String msg;
    public T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 判断返回值是否成功
     * @return
     */
    public boolean isSucceed(){
        if(code.equals("0"))
            return true;
        else
            return false;
    }
}
