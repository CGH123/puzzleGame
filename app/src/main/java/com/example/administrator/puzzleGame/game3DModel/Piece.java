package com.example.administrator.puzzleGame.game3DModel;


public interface Piece extends Draw {
    /**
     * 交换两块拼图位置
     *
     * @param body 拼图块2
     */
    void swap(PieceBody body);

    /**
     * @param x 屏幕坐标X
     * @param y 屏幕坐标Y
     * @return 拼图块编号
     */
    float pickUpTime(float x, float y);

    /**
     * @param pieceNum 编号
     * @return 是否编号相等
     */
    Boolean isEqualNum(int pieceNum);

    /**
     * 高亮拼图块
     */
    void hint();

    void setDrawLine(Boolean isDrawLine);
}
