package com.fos.fosmvp.common.utils;

import android.util.Log;

/**
 * 日志打印类
 */
public class LogUtils {
	private static boolean isDebug = false;
	private static final String TAG = "LogUtils";

	public static void out(String msg) {
		if (isDebug)
			System.out.println(msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	public static void w(String msg) {
		if (isDebug)
			Log.w(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void setShowLogEnabled(boolean showLogEnabled) {
		LogUtils.isDebug = showLogEnabled;
	}

}
