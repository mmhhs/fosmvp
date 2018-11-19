package com.fos.fosmvp.ui.login.model;

/**
 * Created by ZY on 2017/11/21.
 */

public class LimitEntity {
    /**
     * uid : 用户UID
     * auth : Md5密文
     * userType : 1
     */

    private String uid = "";
    private String auth = "";
    private String userType = "61";

    public LimitEntity() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
