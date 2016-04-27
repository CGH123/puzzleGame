package com.example.administrator.puzzleGame.base;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.example.administrator.puzzleGame.R;
import com.example.administrator.puzzleGame.util.LogUtil;

/**
 * Created by HUI on 2016-04-04.
 */
public class BaseApplication extends Application {
    public static boolean isDebugmode = false;
    private static BaseApplication instance;
    /**
     * 静音、震动默认开关
     **/
    private static boolean isSlient = false;
    private static boolean isVibrate = true;
    private static boolean isPrintLog = false;

    private static SoundPool notiMediaplayer;
    /**
     * 新消息提醒
     **/
    private static int notiSoundPoolID;
    private static Vibrator notiVibrator;


    /**
     * <p/>
     * 获取BaseApplication实例
     * <p/>
     * 单例模式，返回唯一实例
     *
     * @return instance
     */
    public static BaseApplication getInstance() {
        return instance;
    }


    /**
     * 新消息提醒 - 声音提醒、振动提醒
     */
    public static void playNotification() {
        if (!isSlient) {
            notiMediaplayer.play(notiSoundPoolID, 1, 1, 0, 0, 1);
        }
        if (isVibrate) {
            notiVibrator.vibrate(200);
        }

    }

    private void initNotification() {
        notiMediaplayer = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        notiSoundPoolID = notiMediaplayer.load(this, R.raw.crystalring, 1);
        notiVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

        LogUtil.setLogStatus(isPrintLog); // 设置是否显示日志

        initNotification();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.e("BaseApplication", "onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.e("BaseApplication", "onTerminate");
    }
}
