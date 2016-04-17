package com.example.administrator.puzzleGame.game3DModel;


import com.example.administrator.puzzleGame.util.LogUtil;

import java.util.Vector;

public class QuadPlane extends BaseBody implements Object {
    int texId;
    Vector2f[] texturePoints;
    Vector<QuadPiece> quadPieces;
    int row, col;

    public QuadPlane(Vector2f[] points, float size, int texId) {
        super.setBox(size, size, 0);
        this.texturePoints = points;
        this.texId = texId;
        this.row = (int) points[points.length - 1].x;
        this.col = (int) points[points.length - 1].y;
        quadPieces = new Vector<>(row * col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int startPos = i * (col + 1) + j;
                Vector2f[] quadPoints = new Vector2f[]{
                        new Vector2f(points[startPos]),
                        new Vector2f(points[startPos + 1]),
                        new Vector2f(points[startPos + col + 1]),
                        new Vector2f(points[startPos + col + 2])};
                Boolean isOutPlane = false, isAllOutPlane = true;
                float range = size / 2;
                for (int k = 0; k < 4; k++) {
                    if(quadPoints[k].x > range|| quadPoints[k].y > range || quadPoints[k].x < -range || quadPoints[k].y < -range) {
                        isOutPlane = true;
                        if (quadPoints[k].x > range || quadPoints[k].y > range) {
                            quadPoints[k].x = Math.min(quadPoints[k].x, size / 2);
                            quadPoints[k].y = Math.min(quadPoints[k].y, size / 2);
                        }
                        if (quadPoints[k].x < -range || quadPoints[k].y < -range) {
                            quadPoints[k].x = Math.max(quadPoints[k].x, -range);
                            quadPoints[k].y = Math.max(quadPoints[k].y, -range);
                        }
                    }else
                        isAllOutPlane = false;
                }
                if (!isAllOutPlane) {
                    QuadPiece temp = new QuadPiece(quadPieces.size(), quadPoints, texId, isOutPlane);
                    quadPieces.add(temp);
                }
            }
        }
    }


    @Override
    public Boolean isPickUpObject(float locationX, float locationY) {
        float time = this.getCurrBox().pickUpTime(locationX, locationY);
        return !Float.isInfinite(time);
    }

    @Override
    public int pickUpPiece(float x, float y) {
        float minTime = Float.POSITIVE_INFINITY;
        int resultNum = -1;
        for (int i = 0; i < quadPieces.size(); i++) {
            if (quadPieces.get(i) == null) continue;
            float time = quadPieces.get(i).pickUpTime(x, y);
            if (Float.isInfinite(time)) continue;
            if (time < minTime) {
                minTime = time;
                resultNum = i;
            }
        }
        if (quadPieces.get(resultNum).quadPieceFill.isOutPlane) {
            LogUtil.d("pick out piece", resultNum + "");
            resultNum = -1;
        }
        return resultNum;
    }

    @Override
    public void swapPiece(int baseNum1, int baseNum2) {
        if (quadPieces.get(baseNum1) == null || quadPieces.get(baseNum2) == null) return;
        for (int i = 0; i < quadPieces.get(baseNum1).quadPieceFill.texCoors.length; i++) {
            float temp = quadPieces.get(baseNum1).quadPieceFill.texCoors[i];
            quadPieces.get(baseNum1).quadPieceFill.texCoors[i] = quadPieces.get(baseNum2).quadPieceFill.texCoors[i];
            quadPieces.get(baseNum2).quadPieceFill.texCoors[i] = temp;
        }
        int temp = quadPieces.get(baseNum1).quadPieceFill.num;
        quadPieces.get(baseNum1).quadPieceFill.num = quadPieces.get(baseNum2).quadPieceFill.num;
        quadPieces.get(baseNum2).quadPieceFill.num = temp;

        quadPieces.get(baseNum1).quadPieceFill.initBuffer();
        quadPieces.get(baseNum2).quadPieceFill.initBuffer();
    }

    @Override
    public Boolean isCompleted() {
        for (int i = 0; i < quadPieces.size(); i++) {
            if (quadPieces.get(i) == null) continue;
            if (quadPieces.get(i).quadPieceFill.num != i)
                return false;
        }
        return true;
    }

    @Override
    public void hintPiece(int pieceNum) {
        quadPieces.get(pieceNum).quadPieceFill.isCheck = 1 - quadPieces.get(pieceNum).quadPieceFill.isCheck;
    }

    @Override
    public int getPiecesSize() {
        return quadPieces.size();
    }

    @Override
    public void drawSelf() {
        MatrixState.pushMatrix();
        for (QuadPiece quadPiece : quadPieces)
            quadPiece.drawSelf(texId);
        MatrixState.popMatrix();
    }
}
