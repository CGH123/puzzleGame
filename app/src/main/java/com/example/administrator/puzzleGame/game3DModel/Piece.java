package com.example.administrator.puzzleGame.game3DModel;


public interface Piece {

    //拾取距离
    float pickUpTime(float x, float y);

    //初始化顶点坐标与着色数据的方法
    void initVertexData();

    //自定义初始化着色器的initShader方法
    void initShader(int Program);

    //绘制
    void drawSelf(int texId);
}
