package com.fos.fosmvp.entity;

import java.io.Serializable;

/**
 * 接口返回值实体父类
 */
public class ResultEntity implements Serializable {

	public String code="-1";
    public String msg;

    public ResultEntity() {
    }

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
}
