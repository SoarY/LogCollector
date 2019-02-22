package com.mx.logcollect.logcat;

import android.content.Context;
import android.os.Environment;

import com.mx.logcollect.utils.DateUtil;
import com.mx.logcollect.utils.FileSizeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogcatHelper {

    private static final String LOGCAT_FNAME = "Logcat";
    private static final String LOGCAT_NAME = "logcat.log";
    private static final double MAX_SIZE = 5;

    private static LogcatHelper mInstance;
    private LogDumper mLogDumper;
    private File file;

    public static LogcatHelper getInstance() {
        if (mInstance == null)
            synchronized (LogcatHelper.class) {
                if (mInstance == null)
                    mInstance = new LogcatHelper();
            }
        return mInstance;
    }

    private LogcatHelper() {
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return LOGCAT_NAME;
    }

    public void logDelete(boolean isStart) {
        stop();
        file.delete();
        if (isStart)
            start();
    }

    public void init(Context context) {
        File logDir = isSDcardExsit() ? context.getExternalFilesDir(null) : context.getFilesDir();
        file = new File(logDir.getAbsolutePath() + File.separator + LOGCAT_FNAME, getFileName());
    }

    private boolean isSDcardExsit() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public void start() {
        if (mLogDumper == null)
            mLogDumper = new LogDumper(String.valueOf(android.os.Process.myPid()), file);
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private String pid;
        private File file;

        private boolean mRunning = true;
        private FileOutputStream output;
        private BufferedReader mReader;

        public LogDumper(String pid, File file) {
            this.pid = pid;
            this.file = file;
            initStart();
        }

        private void initStart() {
            try {
                String cmds = "logcat  | grep \"(" + pid + ")\"";//打印所有日志信息
                Process logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                output = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                String line;
                while (mRunning && mReader != null && (line = mReader.readLine()) != null) {
                    if (line.length() == 0) continue;
                    double size = FileSizeUtil.getFileOrFilesSize(file, FileSizeUtil.SIZETYPE_MB);
                    if (size > MAX_SIZE) {
                        LogcatHelper.this.stop();
                        file.delete();
                        LogcatHelper.this.start();
                    }
                    if (output != null && line.contains(pid)) {
                        String time = DateUtil.getCurrentTime(DateUtil.DateFormatConstant.GL_TIMESTAMP_FORMAT);
                        output.write((time + "  " + line + "\n").getBytes());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mReader != null) {
                    try {
                        mReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}