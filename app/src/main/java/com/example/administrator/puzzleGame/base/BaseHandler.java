package com.example.administrator.puzzleGame.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Handler
 * Created by guojun on 2015/12/14
 */
public class BaseHandler {
    /**
     * 防止handler对activity有隐式引用，匿名内部类不会对外部类有引用
     */
    public static class UnleakHandler extends Handler {
        private final WeakReference<OnMessageListener> activity;

        public UnleakHandler(OnMessageListener activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (activity.get() == null) {
                return;
            }
            activity.get().processMessage(msg);
        }
    }

    public interface OnMessageListener{
        void processMessage(Message message);
    }
}
