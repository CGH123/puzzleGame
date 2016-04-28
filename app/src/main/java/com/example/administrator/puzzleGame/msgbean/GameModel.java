package com.example.administrator.puzzleGame.msgbean;

import com.example.protocol.Entity;

/**
 * 用来进行游戏设置
 * Created by HUI on 2016-04-28.
 */
public class GameModel extends Entity{

    int gameModel;

    public GameModel(){
        gameModel = -1;
    }

    public GameModel(int gameModel){
        this.gameModel = gameModel;
    }

    public int getGameModel() {
        return gameModel;
    }

    public void setGameModel(int gameModel) {
        this.gameModel = gameModel;
    }

}
