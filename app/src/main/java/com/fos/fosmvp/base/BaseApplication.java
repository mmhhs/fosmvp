package com.fos.fosmvp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.fos.fosmvp.http.Api;

/**
 * Created by mmh on 2018/7/17.
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        Api.initialize();
    }

    public static Context getAppContext() {
        return baseApplication;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
