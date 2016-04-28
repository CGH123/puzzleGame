package com.example.administrator.puzzleGame.msgbean;

import com.example.administrator.puzzleGame.gameModel.Vector2f;
import com.example.protocol.Entity;

/**
 * 用来进行游戏设置
 * Created by HUI on 2016-04-28.
 */
public class GameModel extends Entity {

    private int gameModel;
    private int num;

    private float[] points;

    public GameModel() {
    }

    public GameModel(int gameModel, int num, float[] points) {
        this.gameModel = gameModel;
        this.num = num;
        this.points = points;
    }

    public GameModel(int gameModel, int num, Vector2f[] quadPoints) {
        this.gameModel = gameModel;
        this.num = num;
        points = new float[quadPoints.length * 2];
        int index = 0;
        for(Vector2f vector2f: quadPoints){
            points[index++] = vector2f.x;
            points[index++] = vector2f.y;
        }
    }

    public int getGameModel() {
        return gameModel;
    }

    public void setGameModel(int gameModel) {
        this.gameModel = gameModel;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float[] getPoints() {
        return points;
    }

    public void setPoints(float[] points) {
        this.points = points;
    }
}
