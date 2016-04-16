package com.example.administrator.puzzleGame.game3DModel;


public interface Piece {

    //拾取距离
    float pickUpTime(float x, float y);

    //初始化顶点坐标
    void initVertexData();

    //绘制
    void drawSelf(int texId);
}
