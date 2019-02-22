package com.mx.logcollect.crashlog;

import android.content.Context;
import android.os.Process;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * NAME：YONG_
 * Created at: 2019/2/21
 * Describe:
 */
public class CrashHelper implements UncaughtExceptionHandler {

    private static CrashHelper mInstance;

    private OnExceptioningListener onExceptioninglistener;

    private CrashLogFile crashLogFile;
    private UncaughtExceptionHandler mDefaultCrashHandler;

    private CrashHelper() {
    }

    public static CrashHelper getInstance() {
        if (mInstance == null)
            synchronized (CrashHelper.class) {
                if (mInstance == null)
                    mInstance = new CrashHelper();
            }
        return mInstance;
    }

    public CrashLogFile getCrashLogFile() {
        return crashLogFile;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        crashLogFile = new CrashLogFile(context.getApplicationContext());
        //得到系统的应用异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前应用异常处理器改为默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     *
     * @param thread 为出现未捕获异常的线程
     * @param ex     为未捕获的异常 ，可以通过e 拿到异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //导入异常信息到SD卡中
        dumpExceptionSave(ex);
        //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决Bug
        exceptioningUpload();
        ex.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionSave(Throwable ex) {
        crashLogFile.dumpSave(ex);
    }

    private void exceptioningUpload() {
        if (onExceptioninglistener != null && getCrashLogFile().getFile().exists())
            onExceptioninglistener.onExceptioning(getCrashLogFile().getFile());
    }

    public interface OnExceptioningListener {
        void onExceptioning(File file);
    }

    public void setOnExceptioninglistener(OnExceptioningListener onExceptioninglistener) {
        this.onExceptioninglistener = onExceptioninglistener;
    }
}
