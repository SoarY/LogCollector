package com.soar.logexample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mx.logcollect.crashlog.CrashHelper;
import com.mx.logcollect.logcat.LogcatHelper;
import com.soar.logexample.MyApplication;
import com.soar.logexample.R;

import java.io.File;

/**
 * 测试log日志上传功能——此后可删除此Activity
 */
public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        //        CrashHelper.getInstance().setOnExceptioninglistener(this::uploadCrash);
    }

    private void causeCrash() {
        String s = null;
        s.split("1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                causeCrash();
                break;
            case R.id.button2:
                uploadLogcat(LogcatHelper.getInstance().getFile());
                break;
            case R.id.button3:
                uploadCrash(CrashHelper.getInstance().getCrashLogFile().getFile());
                break;
        }
    }

    private void uploadLogcat(File file) {
        if (!file.exists()) return;
        new Handler().postDelayed(() -> {
            Toast.makeText(MyApplication.getContext(), "logcat日志上传成功", Toast.LENGTH_SHORT).show();
            LogcatHelper.getInstance().logDelete(true);
        }, 1500);
    }

    private void uploadCrash(File file) {
        if (!file.exists()) return;
        new Handler().postDelayed(() -> {
            Toast.makeText(MyApplication.getContext(), "Crash日志上传成功", Toast.LENGTH_SHORT).show();
            CrashHelper.getInstance().getCrashLogFile().logDelete();
        }, 1500);
    }
}
