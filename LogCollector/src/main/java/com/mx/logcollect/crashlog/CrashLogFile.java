package com.mx.logcollect.crashlog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.mx.logcollect.utils.DateUtil;
import com.mx.logcollect.utils.MD5Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * NAME：YONG_
 * Created at: 2019/2/21
 * Describe:
 */
public class CrashLogFile {

    public static final String Crash_FNAME = "CrashLog";
    public static final String Crash_NAME = "crash.log";

    private Context context;
    private File file;

    public CrashLogFile(Context context) {
        init(this.context = context);
    }

    private void init(Context context) {
        File logDir = isSDcardExsit() ? context.getExternalFilesDir(null) : context.getFilesDir();
        file = new File(logDir.getAbsolutePath() + File.separator + Crash_FNAME, getFileName());
    }

    private boolean isSDcardExsit() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public String getFileName() {
        return Crash_NAME;
    }

    public File getFile() {
        return file;
    }

    public void logDelete() {
        file.delete();
    }

    public void dumpSave(Throwable ex) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pw == null) return;

        pw.println("&start---");
        //写入时间
        pw.print("logTime:");
        pw.println(DateUtil.getCurrentTime(DateUtil.DateFormatConstant.GL_TIMESTAMP_FORMAT));
        //写入手机信息
        dumpPhoneInfo(pw);
        //异常MD5
        pw.print("crashMD5:");
        pw.println(MD5Utils.MD5(ex.toString()));
        //异常dump
        pw.print("crashDump:");
        ex.printStackTrace(pw);
        pw.println("&end---");
        pw.println();
        pw.println();
        pw.println();
        pw.close();//关闭输入流
    }

    private void dumpPhoneInfo(PrintWriter pw) {
        PackageInfo pi = getPackageInfo(context);
        //AndroidID
        String AndroidID = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.ANDROID_ID);

        //写入唯一序列号
        pw.print("Android ID: ");
        pw.println(AndroidID);
        //写入APP版本号
        pw.print("App Version:");
        pw.print("[");
        pw.print("versionName:");
        pw.print(pi.versionName);
        pw.print(" versionCode:");
        pw.print(pi.versionCode);
        pw.println("]");
        //写入 Android 版本号
        pw.print("OS Version: ");
        pw.print("[");
        pw.print("Version:");
        pw.print(Build.VERSION.RELEASE);
        pw.print(" SDK:");
        pw.print(Build.VERSION.SDK_INT);
        pw.println("]");
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? Build.SUPPORTED_ABIS : Build.CPU_ABI);
    }

    private PackageInfo getPackageInfo(Context context) {
        //得到包管理器
        PackageManager pm = context.getPackageManager();
        //得到包对象
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi==null?new PackageInfo():pi;
    }

}
