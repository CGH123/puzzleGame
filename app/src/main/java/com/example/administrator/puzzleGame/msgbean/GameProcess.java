package com.example.administrator.puzzleGame.msgbean;

import com.example.protocol.Entity;

/**
 * 游戏对战完成度
 * Created by HUI on 2016-04-23.
 */
public class GameProcess extends Entity {
    private float progress;

    public GameProcess() {
    }

    public GameProcess(float progress) {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
