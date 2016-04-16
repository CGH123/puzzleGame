package com.example.administrator.puzzleGame.game3DModel;


import com.example.administrator.puzzleGame.constant.GameConstant;
import com.example.administrator.puzzleGame.util.VectorUtil;

public class QuadPlane extends BaseBody implements Object {
    int texId;
    Vector2f[] texturePoints;
    Quad[] quads;
    int row, col;

    public QuadPlane(Vector2f[] points, int size, int row, int col, int texId) {
        super.setBox(col * size, row * size, 0);
        this.texturePoints = points;
        this.texId = texId;
        this.row = row;
        this.col = col;
        quads = new Quad[row * col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int startPos = i * (col + 1) + j;
                quads[i * col + j] = new Quad(i * col + j,
                        new Vector2f[]{points[startPos], points[startPos + 1], points[startPos + col + 1], points[startPos + col + 2]},
                        texId);
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
        int resultNum = 0;
        for (int i = 0; i < quads.length; i++) {
            float time = quads[i].pickUpTime(x, y);
            if (Float.isInfinite(time)) continue;
            if (time < minTime) {
                minTime = time;
                resultNum = i;
            }
        }
        return resultNum;
    }

    @Override
    public void swapPiece(int baseNum1, int baseNum2) {
        for (int i = 0; i < quads[baseNum1].texCoors.length; i++) {
            float temp = quads[baseNum1].texCoors[i];
            quads[baseNum1].texCoors[i] = quads[baseNum2].texCoors[i];
            quads[baseNum2].texCoors[i] = temp;
        }
        int temp = quads[baseNum1].num;
        quads[baseNum1].num = quads[baseNum2].num;
        quads[baseNum2].num = temp;

        quads[baseNum1].initBuffer();
        quads[baseNum2].initBuffer();
    }

    @Override
    public Boolean isCompleted() {
        for (int i = 0; i < quads.length; i++) {
            if (quads[i].num != i)
                return false;
        }
        return true;
    }

    @Override
    public void hintPiece(int pieceNum) {
        quads[pieceNum].isCheck = 1 - quads[pieceNum].isCheck;
    }

    @Override
    public void drawSelf() {
        int index = 0;
        float UNIT_SIZE = 0.025f;
        MatrixState.pushMatrix();
        for (float i = (UNIT_SIZE * (row - 1)); i >= -(UNIT_SIZE * (row - 1)); i -= 2 * UNIT_SIZE)
            for (float j = (UNIT_SIZE * (col - 1)); j >= -(UNIT_SIZE * (col - 1)); j -= 2 * UNIT_SIZE) {
                MatrixState.pushMatrix();
                MatrixState.translate(j, i, 0);
                quads[index++].drawSelf(texId);
                MatrixState.popMatrix();
            }
        MatrixState.popMatrix();
    }
}
