package com.soar.logexample;

import android.app.Application;
import android.content.Context;

import com.mx.logcollect.crashlog.CrashHelper;
import com.mx.logcollect.logcat.LogcatHelper;

public class MyApplication extends Application {

    private static Context context;
    private static MyApplication instance;

    public void onCreate() {
        super.onCreate();
        init();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    private void init() {
        instance = this;
        context = getApplicationContext();
        LogcatHelper.getInstance().init(context);
        CrashHelper.getInstance().init(context);
        LogcatHelper.getInstance().start();
    }
}
