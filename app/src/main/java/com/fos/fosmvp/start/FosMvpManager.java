package com.fos.fosmvp.start;

import com.fos.fosmvp.common.http.Api;
import com.fos.fosmvp.common.utils.LogUtils;

/**
 * 框架初始化
 */
public class FosMvpManager {
    /** 提示文字：加载中 */
    public static String TASK_LOADING = "loading...";
    /** 提示文字：没有网络 */
    public static String TASK_NO_NETWORK = "no network";
    /** 提示文字：访问错误，服务器返回值无法解析 */
    public static String TASK_LINK_ERROR = "access error";

    /** 接口访问路径前缀 */
    public static final String PREFIX_URL = "https://bfda-app.ifoton.com.cn/est/";

    /** 是否调试 */
    public static boolean DEBUGGING = true;

    /**
     * 初始化框架
     *
     */
    public static void init(){
        Api.initialize();
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
}
