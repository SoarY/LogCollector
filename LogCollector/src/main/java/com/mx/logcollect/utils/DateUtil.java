package com.mx.logcollect.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * NAME：YONG_
 * Created at: 2017/8/16 14
 * Describe:
 */
public class DateUtil {


    public static class DateFormatConstant {

        /**
         * 默认字符集
         */
        public static final String DEF_CHARSET = "UTF-8";
        /**
         * 服务器上传格式
         */
        public static final String DATE_UPLOAD = "yyyyMM";
        /**
         * 日期统一格式yyyy-mm-dd
         */
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        /**
         * 全局数据用日期格式yyyy/MM/dd
         */
        public static final String GL_DATA_FORMAT_EN = "yyyy/MM/dd";
        /**
         * 全局用日期格式yyyy年MM月dd日
         */
        public static final String GL_DATA_FORMAT = "yyyy年MM月dd日";
        /**
         * 全局用日期格式MM月dd日
         */
        public static final String GL_DATA_FORMAR_YEARMONTH = "MM月dd日";
        /**
         * 全局用月日格式(MM/dd)
         */
        public static final String GL_DATA_FORMAR_MONTH_DAY = "MM/dd";
        /**
         * 全局用年月格式yyyy年MM月
         */
        public static final String GL_DATA_MONTH_FORMAT = "yyyy年MM月";
        /**
         * 全局时间格式(yyyy-MM-dd HH:mm)
         */
        public static final String GL_TIME_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
        /**
         * 全局时间格式(yyyy-MM-dd HH:mm)
         */
        public static final String GL_TIME__MINUTE_FORMAT = "yyyy-MM-dd  HH:mm";
        /**
         * 全局时间格式(yyyy-MM-dd HH:mm:ss)
         */
        public static final String GL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        /**
         * 全局时间戳格式(yyyy-MM-dd HH:mm:ss.SSS)
         */
        public static final String GL_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        /**
         * 全局时间格式(yyyy-MM-dd HH-mm-ss)
         */
        public static final String GL_TIME_ACROSS = "yyyy-MM-dd HH-mm-ss";
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 月份加减——基于当前时间
     *
     * @param month 增加月数(减天数则用负数)
     * @return 计算结果
     */
    public static Date monthPlus(int month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 月份加减。
     *
     * @param base  基础日期
     * @param month 增加月数(减天数则用负数)
     * @return 计算结果
     */
    public static Date monthPlus(Date base, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(base);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 将日期转换成指定格式的字符串。
     *
     * @param date   日期
     * @param format 输出格式
     * @return 日期字符串
     */
    public static String convDate2Str(Date date, String format) {
        if (date == null)
            return "";
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 字符串转时间戳
     *
     * @param timeString 字符串
     * @param format     转换格式
     * @return
     */
    public static String getTime(String timeString, String format) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 时间戳转字符串
     *
     * @param timeStamp 时间戳
     * @param format    转换格式
     * @return
     */
    public static String getStrTime(String timeStamp, String format) {
        if (TextUtils.isEmpty(timeStamp))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long l = Long.valueOf(timeStamp);
        String timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }
}
