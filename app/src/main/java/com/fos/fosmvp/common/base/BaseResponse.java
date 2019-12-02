package com.fos.fosmvp.common.base;

import java.io.Serializable;

/**
 * 封装服务器返回数据
 * TODO 优化参数及请求成功判断
 */
public class BaseResponse<T>  implements Serializable {
    public String code="-1";
    public String msg;
    public String message;
    public T data;
    public int error = -1;
    public String result = "";
    public String failNum;
    public String recordCount;
    public String totalMoney;
    public String access_token;
    public String token;
    public String type="";

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

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFailNum() {
        return failNum;
    }

    public void setFailNum(String failNum) {
        this.failNum = failNum;
    }

    public String getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 判断返回值是否成功
     * @return
     */
    public boolean isSucceed(){
        if(code.equals("0")|| code.equals("200")|| error == 0 || result.equals("0") || "success".equals(""+type))
            return true;
        else
            return false;
    }
}
