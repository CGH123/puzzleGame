package com.example.administrator.puzzleGame.util;

/**
 * 调试的日志操作
 * Created by HUI on 2016-04-04.
 */

import android.util.Log;

public class LogUtil {

    private static boolean isShow = true; // 是否打印日志

    public static void d(String tag, String msg) {
        if (isShow)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isShow)
            Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isShow)
            Log.i(tag, msg);
    }

    public static void setLogStatus(boolean flag) {
        isShow = flag;
    }

    public static void v(String tag, String msg) {
        if (isShow)
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isShow)
            Log.w(tag, msg);
    }

    public static void wtf(String tag, String msg) {
        if (isShow)
            Log.wtf(tag, msg);
    }

}
