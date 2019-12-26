package com.fos.fosmvp.start;

import com.fos.fosmvp.common.http.Api;
import com.fos.fosmvp.common.http.EncryptListener;
import com.fos.fosmvp.common.utils.LogUtils;

/**
 * 框架初始化
 */
public class FosMvpManager {
    /** 提示文字：加载中 */
    public static String TASK_LOADING = "loading...";
    /** 提示文字：没有网络 */
    public static String TASK_NO_NETWORK = "请检查您的手机是否联网";
    /** 提示文字：访问错误，服务器返回值无法解析 */
    public static String TASK_LINK_ERROR = "访问错误，服务器返回值无法解析";

    /** 接口访问路径前缀 */
    public static final String PREFIX_URL = "https://bfda-app.ifoton.com.cn/est/";

    /** 是否调试 */
    public static boolean DEBUGGING = true;

    /**
     * 初始化框架
     *
     */
    public static void init(String prefixUrl){
        Api.initialize(prefixUrl);
        LogUtils.setShowLogEnabled(DEBUGGING);

    }

    /**
     * 设置调试模式
     * @param DEBUGGING 是：true，否：false
     */
    public static void setDEBUGGING(boolean DEBUGGING) {
        FosMvpManager.DEBUGGING = DEBUGGING;
        LogUtils.setShowLogEnabled(DEBUGGING);
    }



    public static void setReadTimeOut(int readTimeOut) {
        Api.setReadTimeOut(readTimeOut);
    }

    public static void setConnectTimeOut(int connectTimeOut) {
        Api.setConnectTimeOut(connectTimeOut);
    }

    public static void setApiParamKey(String key) {
        Api.setJsonKey(key);
    }

    public static void setEncryptListener(EncryptListener encryptListener) {
        Api.setEncryptListener(encryptListener);
    }
}
