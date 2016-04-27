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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameProcess that = (GameProcess) o;

        return Float.compare(that.progress, progress) == 0;

    }

    @Override
    public int hashCode() {
        return (progress != +0.0f ? Float.floatToIntBits(progress) : 0);
    }
}
