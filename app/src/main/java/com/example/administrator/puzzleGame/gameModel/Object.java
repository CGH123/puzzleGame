package com.example.administrator.puzzleGame.gameModel;

public interface Object extends Draw {
    void initPieces();

    /**
     * @param locationX 屏幕坐标X
     * @param locationY 屏幕坐标Y
     * @return 是否拾取
     */
    Boolean isPickUpObject(float locationX, float locationY);

    /**
     * 得到拼图块数目
     */
    int getPiecesSize();

    /**
     * @param x 屏幕坐标X
     * @param y 屏幕坐标Y
     * @return 拼图块编号
     */
    int pickUpPiece(float x, float y);

    /**
     * 交换两块拼图位置
     *
     * @param baseNum1 拼图块编号1
     * @param baseNum2 拼图块编号2
     */
    void swapPiece(int baseNum1, int baseNum2);

    /**
     * @return 拼图是否完成
     */
    float getCompletedProgress();

    /**
     * 高亮拼图块
     *
     * @param pieceNum
     */
    void hintPiece(int pieceNum);

    void setDrawLine(Boolean isDrawLine);
}