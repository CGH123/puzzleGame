package com.example.administrator.puzzleGame.game3DModel;


import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.administrator.puzzleGame.util.VectorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class QuadPiece implements Piece {
    QuadPieceFill quadPieceFill;
    QuadPieceLine quadPieceLine;

    public QuadPiece(int num, Vector2f[] points, int texId, Boolean isOutPlane) {
        quadPieceFill = new QuadPieceFill(num, points, texId, isOutPlane);
        quadPieceLine = new QuadPieceLine(points);
    }

    @Override
    public float pickUpTime(float x, float y) {
        return quadPieceFill.pickUpTime(x, y);
    }


    @Override
    public void drawSelf(int texId) {
        //绘制纹理矩形
        MatrixState.pushMatrix();
        quadPieceFill.drawSelf(texId);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, 0.1f);
        quadPieceLine.drawSelf();
        MatrixState.popMatrix();
    }
}
